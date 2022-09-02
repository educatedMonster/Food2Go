package com.example.kafiesta.network

import androidx.databinding.library.BuildConfig
import com.example.kafiesta.constants.ServerConst.API_SERVER_URL
import com.example.kafiesta.domain.ListNetworktest
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface AppService {

    // region profile
    @Multipart
    @POST("login")
    fun loginAsync(
        @PartMap params: HashMap<String, RequestBody>,
    ): Deferred<LoginBaseNetwork>


    @GET("auth/logout")
    fun onLogoutAsync(
        @Header("Authorization") bearer: String,
    ): Deferred<Any>


    @GET("user")
    fun getProfileAsync(
        @Header("Authorization") bearer: String,
    ): Deferred<UserBaseNetwork>


    //  https://kafiesta-api.osc-fr1.scalingo.io/v1/user/{id}
    @GET("user/{id}")
    fun onGetUserIdAsync(
        @Header("Authorization") bearer: String,
        @Path("id") userId: Long,
    ): Deferred<UserBaseNetwork>


    //    https://kafiesta-api.osc-fr1.scalingo.io/v1/user
    // This APi is use to get Profile of the current user. For security purposes.
    @GET("user")
    fun onGetMedAsync(
        @Header("Authorization") bearer: String,
    ): Deferred<UserBaseNetwork>


    //https://kafiesta-api.osc-fr1.scalingo.io/v1/product/upload/{id}
    @Multipart
    @POST("user/upload/{user_shop_id}")
    fun onUploadImageAsync(
        @Header("Authorization") bearer: String,
        @Path("user_shop_id") userShopId: Long,
        @Part params: MultipartBody.Part,
    ): Deferred<Any>


    // https://kafiesta-api.osc-fr1.scalingo.io/v1/user/update
    @Multipart
    @POST("user/update")
    fun onUpdateUserInfoAsync(
        @Header("Authorization") bearer: String,
        @PartMap params: HashMap<String, RequestBody>,
    ): Deferred<UserBaseNetwork>


    @POST("user/list")
    fun getAllUsersAsync(
        @Header("Authorization") bearer: String,
    ): Deferred<List<Any>>
    //endregion

    // region product and inventory
    //https://kafiesta-api.osc-fr1.scalingo.io/v1/product
    @Multipart
    @POST("product")
    fun onAddProductAsync(
        @Header("Authorization") bearer: String,
        @PartMap params: HashMap<String, RequestBody>,
    ): Deferred<ProductBaseNetwork>


    //https://kafiesta-api.osc-fr1.scalingo.io/v1/product/{id}
    @DELETE("product/{id}")
    fun onDeleteProductAsync(
        @Header("Authorization") bearer: String,
        @Path("id") productId: Long,
    ): Deferred<ProductBaseNetwork>


    //https://kafiesta-api.osc-fr1.scalingo.io/v1/inventory/remove/{product_id}
    @DELETE("inventory/remove/{product_id}")
    fun onRemoveInventoryAsync(
        @Header("Authorization") bearer: String,
        @Path("product_id") productId: Long,
    ): Deferred<InventoryBaseNetwork>


    //https://kafiesta-api.osc-fr1.scalingo.io/v1/product
    @Multipart
    @POST("product/update")
    fun onEditProductAsync(
        @Header("Authorization") bearer: String,
        @PartMap params: HashMap<String, RequestBody>,
    ): Deferred<Any>


    //https://kafiesta-api.osc-fr1.scalingo.io/v1/product/list
    @Multipart
    @POST("product/list")
    fun onAddProductListAsync(
        @Header("Authorization") bearer: String,
        @PartMap params: HashMap<String, RequestBody>,
    ): Deferred<ListNetworktest>


    //https://kafiesta-api.osc-fr1.scalingo.io/v1/product/upload/{id}
    @Multipart
    @POST("product/upload/{id}")
    fun onAddProductImageAsync(
        @Header("Authorization") bearer: String,
        @Path("id") productId: Long,
        @Part params: MultipartBody.Part,
    ): Deferred<Any>


    //https://kafiesta-api.osc-fr1.scalingo.io/v1/product/getProductsForInventory
    @Multipart
    @POST("product/getProductsForInventory")
    fun onGetAllProductInventoryAsync(
        @Header("Authorization") bearer: String,
        @PartMap params: HashMap<String, RequestBody>,
    ): Deferred<ProductInventoryBaseNetwork>


    //https://kafiesta-api.osc-fr1.scalingo.io/v1/inventory/list
    @Multipart
    @POST("inventory/list")
    fun onGetAllInventoryAsync(
        @Header("Authorization") bearer: String,
        @PartMap params: HashMap<String, RequestBody>,
    ): Deferred<InventoryBaseNetwork>


    //https://kafiesta-api.osc-fr1.scalingo.io/v1/inventory/add/10
    @POST("inventory/add/{product_id}")
    fun onAddInventoryAsync(
        @Header("Authorization") bearer: String,
        @Path("product_id") productId: Long,
    ): Deferred<InventoryBaseNetwork>


    // https://kafiesta-api.osc-fr1.scalingo.io/v1/product
    @Multipart
    @POST("inventory/modifyQuantity")
    fun onModifyQuantityAsync(
        @Header("Authorization") bearer: String,
        @PartMap params: HashMap<String, RequestBody>,
    ): Deferred<InventoryBaseNetwork>


    // https://kafiesta-api.osc-fr1.scalingo.io/v1/product
    @Multipart
    @POST("inventory/add")
    fun onInventoryAndQuantityAsync(
        @Header("Authorization") bearer: String,
        @PartMap params: HashMap<String, RequestBody>,
    ): Deferred<InventoryBaseNetwork>
    //endregion

    // region order
    //https://kafiesta-api.osc-fr1.scalingo.io/v1/orders
    @Multipart
    @POST("orders")
    fun onGetAllOrdersAsync(
        @Header("Authorization") bearer: String,
        @PartMap params: HashMap<String, RequestBody>,
    ): Deferred<OrderListBaseNetwork>


    //https://kafiesta-api.osc-fr1.scalingo.io/v1/orders/move
    @Multipart
    @POST("orders/move")
    fun onOrderMoveStatusAsync(
        @Header("Authorization") bearer: String,
        @PartMap params: HashMap<String, RequestBody>,
    ): Deferred<SpecificOrderBaseNetwork>


    //  https://kafiesta-api.osc-fr1.scalingo.io/v1/orders/getOrder/{order_id}
    @GET("orders/getOrder/{order_id}")
    fun onGetOrderIdAsync(
        @Header("Authorization") bearer: String,
        @Path("order_id") orderId: Long,
    ): Deferred<SpecificOrderBaseNetwork>
    //endregion

}

object AppNetwork {
    private val gsonBuilder = GsonBuilder()
        .setLenient()
        .create()

    private val interceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG)
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
