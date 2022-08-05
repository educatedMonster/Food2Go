package com.example.kafiesta.network

import com.example.kafiesta.BuildConfig
import com.example.kafiesta.constants.ServerConst.API_SERVER_URL
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface AppService {

    @Multipart
    @POST("login")
    fun loginAsync(
        @PartMap params: HashMap<String, RequestBody>,
    ): Deferred<UserBaseNetwork>

    @GET("auth/logout")
    fun onLogoutAsync(
        @Header("Authorization") bearer: String,
    ): Deferred<Any>

    @GET("me")
    fun getProfileAsync(
        @Header("Authorization") bearer: String,
    ): Deferred<Any>

    @Multipart
    @GET("user/{id}")
    fun getUserIdAsync(
        @Header("Authorization") bearer: String,
        @Path("id") userId: Int,
    ): Deferred<Any>


    @POST("user/list")
    fun getAllUsersAsync(
        @Header("Authorization") bearer: String,
    ): Deferred<List<Any>>

}

object AppNetwork {
    private val gsonBuilder = GsonBuilder()
        .setLenient()
        .create()

    private val interceptor = HttpLoggingInterceptor().apply {
        level = if(BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

    private val client = OkHttpClient.Builder()
        .writeTimeout(30, java.util.concurrent.TimeUnit.MINUTES)
        .readTimeout(30, java.util.concurrent.TimeUnit.MINUTES)
        .addInterceptor(interceptor)
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            return@addInterceptor chain.proceed(newRequest)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(client)
        .build()

    val service: AppService = retrofit.create(AppService::class.java)
}
