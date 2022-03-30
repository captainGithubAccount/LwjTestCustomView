package com.lwj.lwjtest_coustomview

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object NetUtils {
    fun createRequest(baseUrl: String): Request{
        return Request.Builder()
            .url(baseUrl)
            .build()
    }

    fun getOkHttpClient(): OkHttpClient{
        val builder: OkHttpClient.Builder =  OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .pingInterval(10, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        return builder.build()
    }

}