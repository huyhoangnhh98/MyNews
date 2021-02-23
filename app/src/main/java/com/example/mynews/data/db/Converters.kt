package com.example.mynews.data.db

import androidx.room.TypeConverter
import com.example.mynews.data.response.Source

//convert type source in Article (Source) to String
class Converters {

    @TypeConverter
    fun fromSource(source: Source): String{
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source{
        return Source(name, name)
    }
}