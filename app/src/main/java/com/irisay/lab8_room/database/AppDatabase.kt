package com.irisay.lab8_room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.irisay.lab8_room.database.market.MarketDAO
import com.irisay.lab8_room.database.market.MarketEntity

@Database(entities = [MarketEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMarketDao(): MarketDAO
}