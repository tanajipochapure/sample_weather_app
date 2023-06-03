package com.mphasis.tanaji.weatherapp.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketException
import java.util.concurrent.TimeUnit

object RetrofitClient {
    fun getClient(url: String): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(5, TimeUnit.MINUTES)
        okHttpClient.readTimeout(5, TimeUnit.MINUTES)
        okHttpClient.writeTimeout(5, TimeUnit.MINUTES)
        okHttpClient.addInterceptor(HeaderInterceptor)
        val retrofit = Retrofit.Builder()
        retrofit.baseUrl(url)
        retrofit.client(okHttpClient.build())
        retrofit.addConverterFactory(GsonConverterFactory.create())
        return retrofit.build()
    }

    object HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            try {
                val newBuilder = request.newBuilder()
                return chain.proceed(newBuilder.build())
            } catch (e: Exception) {

                var msg = ""
                var interceptorCode = 0
                when (e) {
                    is SocketException -> {
                        msg = "Socket timeout error"
                        interceptorCode = 408
                    }
                }
                return Response.Builder().request(request).protocol(Protocol.HTTP_1_1)
                    .code(interceptorCode).message(msg).body("{${e}}".toResponseBody(null)).build()
            }
        }
    }
}