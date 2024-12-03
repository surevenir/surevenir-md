package com.capstone.surevenir.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.capstone.surevenir.data.Entity.ProductDatabase
import com.capstone.surevenir.data.dao.ProductDao
import com.capstone.surevenir.helper.Converters
import com.capstone.surevenir.helper.DateConverter
//import com.capstone.surevenir.data.Entity.ProductDatabase
//import com.capstone.surevenir.data.dao.ProductDao
//import com.capstone.surevenir.helper.CategoryConverter
//import com.capstone.surevenir.helper.ImageConverter
import com.capstone.surevenir.model.Product

@Database(entities = [ProductDatabase::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}

