package com.capstone.surevenir.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.ProductData
import com.capstone.surevenir.data.network.response.ProductResponse
import com.capstone.surevenir.data.repository.ProductRepository
import com.capstone.surevenir.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _filteredProducts = MutableStateFlow<List<ProductData>>(emptyList())
    val filteredProducts: StateFlow<List<ProductData>> = _filteredProducts

    var minPriceFilter by mutableStateOf<Double?>(null)
    var maxPriceFilter by mutableStateOf<Double?>(null)
    var startDateFilter by mutableStateOf<Long?>(null)
    var endDateFilter by mutableStateOf<Long?>(null)
    var minStockFilter by mutableStateOf<Int?>(null)


    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _productResponse = MutableLiveData<ProductResponse>()
    val productsResponse: LiveData<ProductResponse?> get() = _productResponse

    private val _searchResults = MutableStateFlow<List<ProductData>>(emptyList())
    val searchResults: StateFlow<List<ProductData>> = _searchResults.asStateFlow()

    private val _products = MutableStateFlow<List<ProductData>?>(null)
    val products: StateFlow<List<ProductData>?> = _products.asStateFlow()

    private val _merchantProducts = MutableStateFlow<List<ProductData>>(emptyList())
    val merchantProducts: StateFlow<List<ProductData>> = _merchantProducts.asStateFlow()

    private val _categoryProducts = MutableStateFlow<List<ProductData>>(emptyList())
    val categoryProducts: StateFlow<List<ProductData>> = _categoryProducts.asStateFlow()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getProducts(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = productRepository.getProducts(token)
                if (response.isSuccessful) {
                    response.body()?.data?.forEach { product ->
                        productRepository.insertProduct(product)
                    }
                    val allProducts = productRepository.getAllProducts()
                    withContext(Dispatchers.Main) {
                        _products.value = allProducts
                    }
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error fetching products", e)
            }
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            _searchQuery.value = query
            if (query.isEmpty()) {
                _searchResults.value = emptyList()
                return@launch
            }
            val filteredProducts = _products.value!!.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true)
            }
            _searchResults.value = filteredProducts
        }
    }
    fun getProductsByMerchantId(merchantId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val products = productRepository.getProductsByMerchantId(merchantId)
            withContext(Dispatchers.Main) {
                _merchantProducts.value = products
            }
        }
    }

    fun getProductsByCategoryId(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val products = productRepository.getProductsByCategoryId(categoryId)
            withContext(Dispatchers.Main) {
                _categoryProducts.value = products  // Menggunakan _categoryProducts bukan _merchantProducts
            }
        }
    }

    fun insertProduct(product: ProductData) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.insertProduct(product)
        }
    }

    fun updateProduct(product: ProductData) {
        viewModelScope.launch {
            productRepository.updateProduct(product)
        }
    }

    fun deleteProduct(product: ProductData) {
        viewModelScope.launch {
            productRepository.deleteProduct(product)
        }
    }

    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val allProducts = productRepository.getAllProducts()
            withContext(Dispatchers.Main) {
                _products.value = allProducts
            }
        }
    }
     fun applyFilter() {
        viewModelScope.launch {
            // Pindah ke thread IO sebelum memanggil repository
            val filteredList = withContext(Dispatchers.IO) {
                productRepository.getFilteredProducts(
                    minPrice = minPriceFilter,
                    maxPrice = maxPriceFilter,
                    startDate = startDateFilter,
                    endDate = endDateFilter,
                    minStock = minStockFilter
                )
            }
            // Di sini gunakan productDatabaseToProduct dari repository:
            val productDataList = filteredList.map { productDb ->
                // Karena productDatabaseToProduct() private di repository,
                // Anda perlu menambahkan fungsi publik di repository untuk memetakannya
                productRepository.convertProductDatabaseToProduct(productDb)
            }
            _filteredProducts.value = productDataList
        }
    }


//    fun getProductById(productId: Int): ProductData? {
//        return productRepository.getProductById(productId)
//    }
}
