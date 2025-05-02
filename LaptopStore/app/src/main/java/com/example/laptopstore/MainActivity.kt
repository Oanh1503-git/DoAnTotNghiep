package com.example.laptopstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.laptopstore.navigation.NavigationGraph
import com.example.laptopstore.ui.theme.LaptopStoreTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var navController: NavHostController
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaptopStoreTheme {
                navController= rememberNavController()
                NavigationGraph(navController)
            }
        }
    }
}
