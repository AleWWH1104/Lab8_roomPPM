package com.irisay.lab8_room.ui.marketproducts.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.irisay.lab8_room.database.market.MarketEntity
import com.irisay.lab8_room.navigation.NavigationState
import com.irisay.lab8_room.ui.marketproducts.viewmodel.MarketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketList(viewModel: MarketViewModel, navController: NavController) {
    val products = viewModel.marketItems.observeAsState(initial = emptyList())
    val isLoading = viewModel.isLoading.observeAsState(initial = false)

    LaunchedEffect(Unit) {
        viewModel.fetchProducts()
    }

    Scaffold(
        topBar = {
            AppBar(title = "SuperMarket", navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD5B9B2))
                .padding(paddingValues)
        ) {
            if (isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (products.value.isEmpty()) {
                Text("No hay productos disponibles.", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(products.value) { product ->
                        ListCard(
                            item = product,
                            onEdit = {
                                navController.navigate("editProduct/${product.id}")
                            },
                            onDelete = { viewModel.deleteProduct(product) }
                        )
                    }
                }
            }
        }
    }
}



