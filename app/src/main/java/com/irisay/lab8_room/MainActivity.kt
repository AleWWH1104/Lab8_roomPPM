package com.irisay.lab8_room

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.irisay.lab8_room.navigation.AppNavigation
import com.irisay.lab8_room.ui.marketproducts.viewmodel.MarketViewModel
import com.irisay.lab8_room.ui.marketproducts.viewmodel.MarketViewModelFactory
import com.irisay.lab8_room.ui.theme.Lab8_roomTheme
import java.io.File

class MainActivity : ComponentActivity() {
    private lateinit var marketViewModel: MarketViewModel

    // lanzador para la cámara
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = (applicationContext as MyApp).marketRepository
        marketViewModel = ViewModelProvider(
            this,
            MarketViewModelFactory(application, repository)
        )[MarketViewModel::class.java]

        // configurar el lanzador de la cámara
        setupCameraLauncher()

        enableEdgeToEdge()
        setContent {
            Lab8_roomTheme {
                Surface {
                    AppNavigation(marketViewModel, navController = rememberNavController())
                }
            }
        }
    }

    // configuración del lanzador de la cámara para capturar imágenes
    private fun setupCameraLauncher() {
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                marketViewModel.currentPhotoPath?.let { path ->
                    marketViewModel.saveImagePath(path) // Guardar la ruta en la base de datos
                }
            }
        }
    }

    // metodo para lanzar la cámara y capturar una imagen
    private fun launchCamera() {
        val photoFile: File = marketViewModel.createImageFile()
        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "com.irisay.lab8_room.fileprovider",
            photoFile
        )
        takePictureLauncher.launch(photoURI)
    }

    fun checkPermissionsAndLaunchCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            launchCamera()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}
