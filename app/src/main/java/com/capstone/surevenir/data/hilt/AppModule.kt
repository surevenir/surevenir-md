package com.capstone.surevenir.data.hilt

import android.content.Context
import androidx.room.Room
import com.capstone.surevenir.data.dao.ProductDao
import com.capstone.surevenir.data.database.AppDatabase
import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.repository.ReviewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "product-database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductDao(appDatabase: AppDatabase): ProductDao {
        return appDatabase.productDao()
    }

    @Provides
    @Singleton
    fun provideReviewsRepository(
        apiService: ApiService,
        @ApplicationContext context: Context
    ): ReviewsRepository {
        return ReviewsRepository(apiService, context)
    }
}