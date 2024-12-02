package com.capstone.surevenir.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.capstone.surevenir.data.Entity.ProductDatabase
import com.capstone.surevenir.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: ProductDatabase)

    @Update
    fun update(product: ProductDatabase)

    @Delete
    fun delete(product: ProductDatabase)

    @Query("SELECT * FROM products")
    fun getAllProducts(): List<ProductDatabase>

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductById(productId: Int): ProductDatabase?

    @Query("SELECT * FROM products WHERE merchantId = :merchantId")
    fun getProductsByMerchantId(merchantId: Int): List<ProductDatabase>
}



