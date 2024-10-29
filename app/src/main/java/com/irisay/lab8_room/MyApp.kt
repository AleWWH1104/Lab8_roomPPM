package com.irisay.lab8_room

import android.app.Application
import androidx.room.Room
import com.irisay.lab8_room.database.AppDatabase
import com.irisay.lab8_room.ui.marketproducts.repository.MarketRepository

class MyApp: Application() {
    // Singleton instance of the Room database
    private lateinit var database: AppDatabase
        private set

    lateinit var marketRepository: MarketRepository
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "market_products"
        ).build()

        marketRepository = MarketRepository(
            database.getMarketDao()
        )
    }
}