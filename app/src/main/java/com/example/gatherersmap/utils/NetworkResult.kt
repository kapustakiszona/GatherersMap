package com.example.gatherersmap.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed class NetworkResult<T : Any> {
    class Success<T : Any>(val data: T) : NetworkResult<T>()
    class Error<T : Any>(val errorMessage: String) : NetworkResult<T>()
}

suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>,
): NetworkResult<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                NetworkResult.Success(body)
            } else {
                val errorResponse =
                    Json.decodeFromString<ErrorResponse>(response.errorBody()!!.string())
                NetworkResult.Error(
                    errorMessage = errorResponse.errorMessage
                )
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            NetworkResult.Error(
                errorMessage = "${e.message}"
            )
        } catch (e: IOException) {
            e.printStackTrace()
            NetworkResult.Error(
                errorMessage = "Check your network connection"
            )
        } catch (e: Throwable) {
            e.printStackTrace()
            NetworkResult.Error(errorMessage = "Something went wrong")
        }
    }
}

@Serializable
data class ErrorResponse(
    val errorCode: String,
    val errorMessage: String,
)