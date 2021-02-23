package com.example.mynews.data.api

import com.example.mynews.data.response.NewsResponse
import com.example.mynews.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") country : String = "us",
        @Query("page") page : Int = 1,
        @Query("apiKey") apiKey : String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") searchQuery: String,
        @Query("page") page : Int = 1,
        @Query("apiKey") apiKey : String = API_KEY
    ): Response<NewsResponse>
}