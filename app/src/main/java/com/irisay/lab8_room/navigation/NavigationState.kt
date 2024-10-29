package com.irisay.lab8_room.navigation

sealed class NavigationState(val route: String)  {
    data object AllMarketProducts: NavigationState("marketProducts")
    data object AddProducts: NavigationState("addProducts")
}