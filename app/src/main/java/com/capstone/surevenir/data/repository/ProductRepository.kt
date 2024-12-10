package com.capstone.surevenir.data.repository

//import com.capstone.surevenir.data.dao.ProductDao
import android.util.Log
import com.capstone.surevenir.data.entity.ProductDatabase
import com.capstone.surevenir.data.dao.ProductDao
import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.Category
import com.capstone.surevenir.data.network.response.ImageData
import com.capstone.surevenir.data.network.response.ProductCategory
import com.capstone.surevenir.data.network.response.ProductData
import com.capstone.surevenir.data.network.response.ProductDetailResponse
import com.capstone.surevenir.data.network.response.ProductResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val apiService: ApiService,
    private val productDao: ProductDao

) {

    suspend fun getFilteredProducts(
        minPrice: Double? = null,
        maxPrice: Double? = null,
        startDate: Long? = null,
        endDate: Long? = null,
        minStock: Int? = null
    ): List<ProductDatabase> {
        return productDao.getFilteredProducts(
            minPrice,
            maxPrice,
            startDate,
            endDate,
            minStock
        )
    }


//    suspend fun getProducts(token: String): Response<ProductResponse> {
//        return apiService.getProducts(token)
//    }

    suspend fun getProductDetail(productId: Int, token: String): ProductDetailResponse {
        return apiService.getProductDetail(productId, "Bearer $token")
    }

    suspend fun insertProduct(product: ProductData) {
        withContext(Dispatchers.IO) {
            try {
                Log.d("ProductRepo", "Inserting product: ${product.id}")
                val productDb = productToProductDatabase(product)
                productDao.insert(productDb)
                Log.d("ProductRepo", "Product inserted successfully: ${product.id}")
            } catch (e: Exception) {
                Log.e("ProductRepo", "Error inserting product: ${product.id}", e)
                throw e
            }
        }
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

    fun getProductsByCategoryId(categoryId: Int): List<ProductData> {
        return productDao.getProductsByCategoryId(categoryId).map { productDatabaseToProduct(it) }
    }

    fun convertProductDatabaseToProduct(productDb: ProductDatabase): ProductData {
        return productDatabaseToProduct(productDb)
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
    val categoryIds = product.product_categories.mapNotNull { it.category.id }.joinToString(",")

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
        categories = categoryIds,
        images = product.images.joinToString(",") { it.url }
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
            product_categories = productDb.categories.split(",").filter { it.isNotEmpty() }.map {
                ProductCategory(Category(it.toInt(), ""))
            },
            images = productDb.images.split(",").filter { it.isNotEmpty() }.map { ImageData(it) }
        )
    }

    // Di ProductRepository atau ViewModel
    suspend fun debugProductInsertion() {
        val testProduct = ProductDatabase(
            id = 1,  // Karena ini PrimaryKey dan tidak autoGenerate
            slug = "test-product",
            name = "Test Product",
            description = "Test Description",
            price = 1000,
            stock = 10,
            merchantId = 1,
            categories = "1",
            createdAt = System.currentTimeMillis().toString(), // Convert to String karena di entity typenya String
            updatedAt = System.currentTimeMillis().toString(),
            images = "" // Kosong atau berikan string URL gambar
        )

        try {
            productDao.debugInsert(testProduct)
            val allProducts = productDao.getAllProducts()
            Log.d("ProductDebug", "Total products after insertion: ${allProducts.size}")
            allProducts.forEach {
                Log.d("ProductDebug", "Product: $it")
            }
        } catch (e: Exception) {
            Log.e("ProductDebug", "Error testing product insertion", e)
        }
    }
}
