package com.capstone.surevenir.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.capstone.surevenir.data.entity.MerchantDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface MerchantDao {
    @Query("SELECT * FROM merchants")
    fun getAllMerchants(): Flow<List<MerchantDatabase>>

    @Query("SELECT * FROM merchants WHERE id = :merchantId")
    fun getMerchantById(merchantId: Int): Flow<MerchantDatabase?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertMerchants(merchants: List<MerchantDatabase>)

    @Query("SELECT * FROM merchants WHERE market_id = :marketId") // Gunakan market_id sesuai nama kolom di database
    fun getMerchantsByMarketId(marketId: Int): Flow<List<MerchantDatabase>>

    @Query("DELETE FROM merchants")
     fun deleteAllMerchants()

    @Transaction
     fun updateMerchants(merchants: List<MerchantDatabase>) {
        deleteAllMerchants()
        insertMerchants(merchants)
    }
}