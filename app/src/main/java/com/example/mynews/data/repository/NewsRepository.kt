package com.example.mynews.data.repository

import com.example.mynews.data.api.RetrofitInstance
import com.example.mynews.data.db.ArticleDatabase
import com.example.mynews.data.response.Article
import com.example.mynews.util.Constants

class NewsRepository(
    val db : ArticleDatabase
) {
    //api
    suspend fun getBreakingNews(countryCode : String, pageNumber : Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)


    suspend fun searchNews(searchQuery :  String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    //database
    //put on db
    suspend fun upsert(article : Article) = db.getArticleDao().upsert(article)

    //get from db
    fun getSaveNews() = db.getArticleDao().getAllArticle()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}