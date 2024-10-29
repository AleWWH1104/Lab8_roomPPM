package com.irisay.lab8_room.ui.marketproducts.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.irisay.lab8_room.MainActivity
import com.irisay.lab8_room.database.market.MarketEntity
import com.irisay.lab8_room.navigation.NavigationState
import com.irisay.lab8_room.ui.marketproducts.viewmodel.MarketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProduct(
    viewModel: MarketViewModel,
    navController: NavController,
    productId: Int? = null // ver si el producto existe
) {
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    val imageUri by viewModel.imageUri.collectAsState()
    val context = LocalContext.current as MainActivity

    // si no es null cargar para editar
    LaunchedEffect(productId) {
        if (productId == null) {
            viewModel.clearImageUri() // Limpia la URI cuando se inicia un nuevo producto
        } else {
            viewModel.loadProductById(productId) { product ->
                product?.let { prod ->
                    itemName = prod.itemName
                    quantity = prod.quantity.toString()
                    viewModel.setImageUri(prod.imageUrl?.let { Uri.parse(it) })
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productId == null) "Add New Item" else "Edit Item") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(NavigationState.AllMarketProducts.route) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Navigate back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD5B9B2))
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Captured image",
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(150.dp)
                        .padding(8.dp)
                )
            }
            Button(
                onClick = {
                    context.checkPermissionsAndLaunchCamera()
                },
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA26769),
                )
            ) {
                Text("Take Photo")
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Text(text = "Name Product", color = Color.Black)
            TextField(
                value = itemName,
                onValueChange = { itemName = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFA26769)
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 16.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Quantity", color = Color.Black)
            TextField(
                value = quantity,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        quantity = it
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFA26769)
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(vertical = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = {
                    if (itemName.isNotBlank() && quantity.isNotBlank()) {
                        val product = MarketEntity(
                            id = productId ?: 0, // usar el ID si se edita, o 0 para crear uno nuevo
                            itemName = itemName,
                            quantity = quantity.toIntOrNull() ?: 0,
                            imageUrl = imageUri?.toString()
                        )
                        if (productId == null) {
                            viewModel.addProduct(product.itemName, product.quantity, product.imageUrl) // agregar producto si es nuevo
                        } else {
                            viewModel.updateProduct(product) // actualizar si ya existe
                        }
                        navController.navigate(NavigationState.AllMarketProducts.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA26769))
            ) {
                Text(text = if (productId == null) "Add Product" else "Update Product", color = Color.White)
            }
        }
    }
}
