package com.capstone.surevenir.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.capstone.surevenir.data.entity.ProductDatabase

@Dao
interface ProductDao {

    @Query("""
    SELECT * FROM products
    WHERE (:minPrice IS NULL OR price >= :minPrice)
    AND (:maxPrice IS NULL OR price <= :maxPrice)
    AND (:startDate IS NULL OR createdAt >= :startDate)
    AND (:endDate IS NULL OR createdAt <= :endDate)
    AND (:minStock IS NULL OR stock >= :minStock)
""")
    fun getFilteredProducts(
        minPrice: Double?,
        maxPrice: Double?,
        startDate: Long?,
        endDate: Long?,
        minStock: Int?
    ): List<ProductDatabase>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: ProductDatabase)

    @Update
    fun update(product: ProductDatabase)

    @Delete
    fun delete(product: ProductDatabase)

    @Query("SELECT * FROM products")
    fun getAllProducts(): List<ProductDatabase>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'")
    fun searchProducts(searchQuery: String): List<ProductDatabase>

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductById(productId: Int): ProductDatabase?

    @Query("SELECT * FROM products WHERE merchantId = :merchantId")
    fun getProductsByMerchantId(merchantId: Int): List<ProductDatabase>

    @Query("SELECT * FROM products WHERE categories LIKE '%' || :categoryId || '%'")
    fun getProductsByCategoryId(categoryId: Int): List<ProductDatabase>


}



