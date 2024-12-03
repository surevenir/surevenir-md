package com.capstone.surevenir.helper

import androidx.room.TypeConverter
import com.capstone.surevenir.data.network.response.CategoryData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoryListConverter {
    @TypeConverter
    fun fromString(value: String): List<CategoryData> {
        return try {
            Gson().fromJson(value, object : TypeToken<List<CategoryData>>() {}.type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromList(list: List<CategoryData>): String {
        return try {
            Gson().toJson(list)
        } catch (e: Exception) {
            "[]"
        }
    }
}