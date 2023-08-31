package com.example.gatherersmap.di

import android.app.Application
import androidx.room.Room
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.data.localdb.ItemSpotDatabase
import com.example.gatherersmap.data.network.MushroomApi
import com.example.gatherersmap.data.network.MushroomService
import com.example.gatherersmap.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideItemSpotDatabase(app: Application): ItemSpotDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = ItemSpotDatabase::class.java,
            name = "items_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(BASE_URL: String, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideMushroomService(retrofit: Retrofit): MushroomService =
        retrofit.create(MushroomService::class.java)

    @Singleton
    @Provides
    fun provideMushroomApi(mushroomService: MushroomService) =
        MushroomApi(mushroomService)

    @Singleton
    @Provides
    fun provideItemSpotRepository(db: ItemSpotDatabase, apiService: MushroomApi) =
        ItemSpotRepositoryImpl(localDataSource = db, remoteDataSource = apiService)
}