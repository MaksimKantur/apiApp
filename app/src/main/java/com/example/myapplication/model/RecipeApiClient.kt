package com.example.myapplication.model

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object RecipeApiClient {
    private const val BASE_URL = "https://api.edamam.com/"
    fun create(): RecipeApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(RecipeApiService::class.java)
    }
}

interface RecipeApiService {
    @GET("search")
    fun searchRecipes(
        @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String
    ): Call<RecipeResponse>
}