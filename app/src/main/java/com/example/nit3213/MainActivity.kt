package com.example.nit3213

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nit3213.ui.screens.DashboardScreen
import com.example.nit3213.ui.screens.DetailsScreen
import com.example.nit3213.ui.screens.LoginScreen
import com.example.nit3213.ui.theme.NIT3213Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NIT3213Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val nav = rememberNavController()
                    NavHost(navController = nav, startDestination = "login") {

                        composable("login") {
                            LoginScreen(
                                onSuccess = { nav.navigate("dashboard") { popUpTo("login") { inclusive = true } } }
                            )
                        }

                        composable("dashboard") {
                            DashboardScreen(
                                onOpen = { json ->
                                    val encoded = Uri.encode(json)
                                    nav.navigate("details/$encoded")
                                }
                            )
                        }

                        composable(
                            route = "details/{entityJson}",
                            arguments = listOf(navArgument("entityJson") { type = NavType.StringType })
                        ) {
                            val json = it.arguments?.getString("entityJson").orEmpty()
                            DetailsScreen(json)
                        }
                    }
                }
            }
        }
    }
}
