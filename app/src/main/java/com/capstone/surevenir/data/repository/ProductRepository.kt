package com.capstone.surevenir.data.repository

//import com.capstone.surevenir.data.dao.ProductDao
import com.capstone.surevenir.data.Entity.ProductDatabase
import com.capstone.surevenir.data.dao.ProductDao
import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.ImageData
import com.capstone.surevenir.data.network.response.ProductData
import com.capstone.surevenir.data.network.response.ProductDetailResponse
import com.capstone.surevenir.data.network.response.ProductResponse
import com.capstone.surevenir.model.Product
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val apiService: ApiService,
    private val productDao: ProductDao

) {
    suspend fun getProducts(token: String): Response<ProductResponse> {
        return apiService.getProducts(token)
    }

    suspend fun getProductDetail(productId: Int, token: String): ProductDetailResponse {
        return apiService.getProductDetail(productId, "Bearer $token")
    }

    fun insertProduct(product: ProductData) {
        val productDb = productToProductDatabase(product)
        productDao.insert(productDb)
    }

    fun updateProduct(product: ProductData) {
        val productDb = productToProductDatabase(product)
        productDao.update(productDb)
    }

    fun deleteProduct(product: ProductData) {
        val productDb = productToProductDatabase(product)
        productDao.delete(productDb)
    }

    fun getAllProducts(): List<ProductData> {
        return productDao.getAllProducts().map { productDatabaseToProduct(it) }
    }

    fun getProductsByMerchantId(merchantId: Int): List<ProductData> {
        return productDao.getProductsByMerchantId(merchantId).map { productDatabaseToProduct(it) }
    }

//    fun getProductById(productId: Int): ProductData? {
//        return productDao.getProductById(productId)?.let { productDatabaseToProduct(it) }
//    }
//
//     suspend fun getProductById(productId: Int): ProductData? {
//        val productDb = productDao.getProductById(productId)
//        return productDb?.let { productDatabaseToProduct(it) }
//    }

    private fun productToProductDatabase(product: ProductData): ProductDatabase {
        return ProductDatabase(
            id = product.id,
            slug = product.slug,
            name = product.name,
            description = product.description,
            price = product.price,
            merchantId = product.merchant_id,
            stock = product.stock,
            createdAt = product.createdAt.toString(),
            updatedAt = product.updatedAt.toString(),
            categories = product.categories?.joinToString(",") ?: "",
            images = product.images?.joinToString(",") { it.url } ?: ""  // Extract URL dari ImageData
        )
    }

    private fun productDatabaseToProduct(productDb: ProductDatabase): ProductData {
        return ProductData(
            id = productDb.id,
            slug = productDb.slug,
            name = productDb.name,
            description = productDb.description,
            price = productDb.price,
            merchant_id = productDb.merchantId,
            stock = productDb.stock,
            createdAt = productDb.createdAt,
            updatedAt = productDb.updatedAt,
            categories = productDb.categories.split(",").filter { it.isNotEmpty() },
            images = productDb.images.split(",").filter { it.isNotEmpty() }.map { ImageData(url = it) }  // Convert ke ImageData
        )
    }

}
