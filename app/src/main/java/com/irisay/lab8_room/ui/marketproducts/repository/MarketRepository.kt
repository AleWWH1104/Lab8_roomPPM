package com.irisay.lab8_room.ui.marketproducts.repository

import com.irisay.lab8_room.database.market.MarketDAO
import com.irisay.lab8_room.database.market.MarketEntity

class MarketRepository(private val itemMarketDao: MarketDAO){
    suspend fun getProductsFromEntity(): List<MarketEntity>{
        return itemMarketDao.getAllItems()
    }

    suspend fun insertMarketItem(item: MarketEntity) {
        itemMarketDao.insertMarketItem(item)
    }

    suspend fun updateMarketItem(item: MarketEntity) {
        itemMarketDao.updateMarketItem(item)
    }

    suspend fun deleteMarketItem(item: MarketEntity) {
        itemMarketDao.deleteMarketItem(item)
    }

    suspend fun getProductById(productId: Int): MarketEntity? {
        return itemMarketDao.getProductById(productId)
    }

}