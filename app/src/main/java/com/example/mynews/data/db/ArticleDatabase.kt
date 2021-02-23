package com.example.mynews.data.db

import android.content.Context
import androidx.room.*
import com.example.mynews.data.response.Article

//abstract class extend RoomDB for create actual DB
//entiries : create from number of table --> Article

@Database(
    entities = [Article::class],
    version = 1
)

@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao() : ArticleDao

    companion object {
        @Volatile
        private var instance : ArticleDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()


    }
}