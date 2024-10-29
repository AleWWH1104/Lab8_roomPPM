package com.irisay.lab8_room.ui.marketproducts.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.irisay.lab8_room.navigation.NavigationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, navController: NavController) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(NavigationState.AddProducts.route) }) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Go Add",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFA26769),  // Color de fondo del AppBar
            titleContentColor = Color.White  // Color del título
        )
    )
}