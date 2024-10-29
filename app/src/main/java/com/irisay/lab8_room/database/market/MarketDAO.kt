package com.irisay.lab8_room.database.market

import androidx.room.*

@Dao
interface MarketDAO {
    @Query("SELECT * FROM supermarketDB")
    suspend fun getAllItems(): List<MarketEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarketItem(item: MarketEntity)

    @Update
    suspend fun updateMarketItem(item: MarketEntity)

    @Delete
    suspend fun deleteMarketItem(item: MarketEntity)

    @Query("SELECT * FROM supermarketDB WHERE id = :productId LIMIT 1")
    suspend fun getProductById(productId: Int): MarketEntity?

}