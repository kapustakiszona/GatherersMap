package com.example.gatherersmap.utils

import android.util.Log
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response

sealed class NetworkResult<T : Any> {
    class Success<T : Any>(val data: T) : NetworkResult<T>()
    class Error<T : Any>(val errorResponse: ErrorResponse) : NetworkResult<T>()
}

suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>,
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        } else {
            val gson = Gson()
            val errorResponse: ErrorResponse =
                gson.fromJson(response.errorBody().toString(), ErrorResponse::class.java)
            NetworkResult.Error(
                errorResponse = errorResponse
            )
        }
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        Log.e(TAG, "Error body: $errorBody")
        val gson = Gson()
        val errorResponse: ErrorResponse =
            gson.fromJson(errorBody, ErrorResponse::class.java)
        NetworkResult.Error(
            errorResponse = errorResponse
        )
    } catch (e: Throwable) {
        Log.e(TAG, "Throwable: ${e.message}")
        val gson = Gson()
        val errorResponse: ErrorResponse =
            gson.fromJson(e.message, ErrorResponse::class.java)
        NetworkResult.Error(
            errorResponse = errorResponse
        )
    }
}


data class ErrorResponse(
    val errorCode: String,
    val errorMessage: String,
)