package com.example.mynews.data.response


data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)