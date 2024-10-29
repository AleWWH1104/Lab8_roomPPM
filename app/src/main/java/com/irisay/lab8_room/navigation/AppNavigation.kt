package com.irisay.lab8_room.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.irisay.lab8_room.ui.marketproducts.view.AddProduct
import com.irisay.lab8_room.ui.marketproducts.view.MarketList
import com.irisay.lab8_room.ui.marketproducts.viewmodel.MarketViewModel

@Composable
fun AppNavigation(viewModel: MarketViewModel, navController: NavHostController){
    NavHost(navController = navController, startDestination = NavigationState.AllMarketProducts.route ) {
        composable(route = NavigationState.AllMarketProducts.route){
            MarketList(viewModel,navController)
        }
        composable(route = NavigationState.AddProducts.route){
            AddProduct(viewModel, navController)
        }
        composable("addProduct") {
            AddProduct(viewModel, navController)
        }

        composable("editProduct/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            productId?.let {
                AddProduct(viewModel, navController, productId = it)
            }
        }
    }
}