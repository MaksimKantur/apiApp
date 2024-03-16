package com.example.myapplication.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.viewmodel.RecipeViewModel

sealed class Screen(val route: String) {
    object RecipeSearchScreen : Screen("recipe_search")
    object RecipeDetailScreen : Screen("recipe_detail")
}

@Composable
fun NavGraph(navController: NavHostController, viewModel: RecipeViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screen.RecipeSearchScreen.route
    ) {
        composable(Screen.RecipeSearchScreen.route) {
            RecipeSearchScreen(navController, viewModel)
        }
        composable(
            route = Screen.RecipeDetailScreen.route) {
                RecipeDetailScreen(navController,viewModel)
        }
    }
}


