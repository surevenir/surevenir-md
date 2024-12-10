package com.capstone.surevenir.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.capstone.surevenir.data.dao.MerchantDao
import com.capstone.surevenir.data.dao.NotificationDao
import com.capstone.surevenir.data.entity.ProductDatabase
import com.capstone.surevenir.data.dao.ProductDao
import com.capstone.surevenir.data.entity.MerchantDatabase
import com.capstone.surevenir.data.entity.NotificationDatabase
import com.capstone.surevenir.helper.Converters
import com.capstone.surevenir.helper.DateConverter

//import com.capstone.surevenir.data.Entity.ProductDatabase
//import com.capstone.surevenir.data.dao.ProductDao
//import com.capstone.surevenir.helper.CategoryConverter
//import com.capstone.surevenir.helper.ImageConverter


@Database(
    entities = [
        ProductDatabase::class,
        MerchantDatabase::class,
        NotificationDatabase::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class, DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun merchantDao(): MerchantDao
    abstract fun notificationDao(): NotificationDao

}