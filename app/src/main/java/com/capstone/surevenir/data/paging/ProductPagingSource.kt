package com.capstone.surevenir.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.ProductData

class ProductPagingSource(
    private val apiService: ApiService,
    private val token: String
) : PagingSource<Int, ProductData>() {

    override fun getRefreshKey(state: PagingState<Int, ProductData>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductData> {
        return try {
            val page = params.key ?: 1
            val response = apiService.getProducts("Bearer $token", page, params.loadSize)

            LoadResult.Page(
                data = response.body()?.data ?: emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.body()?.data.isNullOrEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}