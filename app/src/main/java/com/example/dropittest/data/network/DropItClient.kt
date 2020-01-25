package com.example.dropittest.data.network

import com.example.dropittest.util.Utils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DropItClient {

    companion object {

        fun createClient(): DropItService {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Utils.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DropItService::class.java)
        }
    }
}