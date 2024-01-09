package com.heewoong.brewstar

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitInterface {

    @GET("/frappuccino")
    fun getMilk(): Call<JsonArray>

    @GET("/test")
    fun getTest(): Call<List<List<String>>>

    @GET("/myFavorite")
    fun getFavorite(@Query("userId") parameter: String): Call<List<List<String>>>

    @GET("/myCustom")
    fun getMyCustom(@Query("userId") parameter: String): Call<List<List<String>>>

    @GET("/allCustoms")
    fun getAllCustoms(): Call<List<List<String>>>

    @FormUrlEncoded
    @POST("/addCustom")
    fun addCustom(
        @Field("name") name: String,
        @Field("menu") menu: String,
        @Field("category") category: String,
        @Field("custom") custom: String,
        @Field("Description") Description: String,
        @Field("creator") creator: String,
        @Field("creatornum") creatornum: String
    ): Call<Void>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
//        private const val BASE_URL = "http://192.249.30.224:80" //
        private const val BASE_URL = "http://172.10.7.47:80" //

        fun create(): RetrofitInterface {
            val gson : Gson =   GsonBuilder().setLenient().create();

            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitInterface::class.java)
        }
    }
}