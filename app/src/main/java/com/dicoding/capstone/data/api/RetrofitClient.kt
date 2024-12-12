package com.dicoding.capstone.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://cataract-detection-jdwtjam4wq-et.a.run.app/"
    private var retrofit: Retrofit? = null

    val instance: Retrofit
        get() {
            if (retrofit == null) {
                // Configure OkHttpClient with timeouts
                val client = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)  // Time to establish a connection
                    .writeTimeout(30, TimeUnit.SECONDS)    // Time to send request body
                    .readTimeout(30, TimeUnit.SECONDS)     // Time to receive response
                    .build()

                // Build Retrofit instance with OkHttpClient
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)  // Add the OkHttpClient with timeouts
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
}
