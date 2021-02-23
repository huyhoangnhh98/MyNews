package com.example.mynews.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mynews.data.response.Article

@Dao
interface ArticleDao {

    //funtion insert or update, onConflict : replace article, return long : the id was inserted
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article) : Long

    // return all available articles in database, should extend live data
    //article inside of that list change then live data will notify observers
    @Query("SELECT * FROM articles")
    fun getAllArticle() : LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}