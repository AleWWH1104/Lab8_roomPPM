package com.irisay.lab8_room.database.market

import androidx.room.*

@Entity(tableName = "supermarketDB")
data class MarketEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "itemName") val itemName: String,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "imageUrl") val imageUrl: String? = null
)