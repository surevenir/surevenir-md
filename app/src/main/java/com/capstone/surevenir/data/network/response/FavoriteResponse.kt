package com.capstone.surevenir.data.network.response

import com.capstone.surevenir.model.Image
import com.capstone.surevenir.model.Merchant
import com.capstone.surevenir.model.Product

data class FavoriteResponse(
    val success: Boolean,
    val status_code: Int,
    val message: String,
    val data: FavoriteData
)

data class FavoriteData(
    val id: Int,
    val user_id: String,
    val product_id: Int,
    val createdAt: String,
    val updatedAt: String
)


data class FavoriteProductResponse(
    val id: Int,
    val user_id: String,
    val product_id: Int,
    val createdAt: String,
    val updatedAt: String,
    val product: ProductFavorite,  // Gunakan ProductFavorite bukan Product
    val images: List<ImageData>
)

data class GetFavoriteProductsResponse(
    val from_cache: Boolean,
    val success: Boolean,
    val status_code: Int,
    val message: String,
    val timestamp: String,
    val data: FavoriteProductsData
)

data class FavoriteProductsData(
    val products: List<FavoriteProductItem>
)

data class FavoriteProductItem(
    val id: Int,
    val user_id: String,
    val product_id: Int,
    val createdAt: String,
    val updatedAt: String,
    val product: Product,
    val images: List<ImageData>
)

data class ProductFavorite(
    val id: Int,
    val slug: String,
    val name: String,
    val description: String,
    val price: Int,
    val merchant_id: Int,
    val stock: Int,
    val createdAt: String,
    val updatedAt: String,
    val merchant: String,
    val categories: List<ProductCategory>,
    val images: List<ImageData>  // Tambahkan images
)

object FavoriteMapper {
    fun mapResponseToProductData(response: ProductFavorite): ProductData {
        return ProductData(
            id = response.id,
            slug = response.slug,
            name = response.name,
            description = response.description,
            price = response.price,
            merchant_id = response.merchant_id,
            stock = response.stock,
            createdAt = response.createdAt,
            updatedAt = response.updatedAt,
            product_categories = response.categories,  // Sesuaikan dengan categories
            images = emptyList()  // Karena di ProductFavorite tidak ada images, kita beri empty list
        )
    }
}

data class DeleteFavoriteResponse(
    val success: Boolean,
    val status_code: Int,
    val message: String,
    val data: DeleteFavoriteData
)

data class DeleteFavoriteData(
    val id: String
)

