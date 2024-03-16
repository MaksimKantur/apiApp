package com.example.myapplication.model

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await

const val APPID = "105f8fb7"
const val KEY = "4d92afc615644ebbe1203944f6a54a25"

object RecipeRepository {
    private val apiService = RecipeApiClient.create()

    suspend fun searchRecipes(ingredient: String, context: Context): List<Hit>? {
        try {
            val response = apiService.searchRecipes(ingredient, APPID, KEY).await()
            return response.hits
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to fetch recipes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            return null
        }
    }


}