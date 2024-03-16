package com.example.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Hit
import com.example.myapplication.model.RecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    private val _recipes = MutableLiveData<List<Hit>?>()
    val recipes: MutableLiveData<List<Hit>?> get() = _recipes

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun searchRecipes(ingredient: String, context: Context) {
        _isLoading.value = true
        viewModelScope.launch {
            val hits = RecipeRepository.searchRecipes(ingredient, context)
            _recipes.value = hits
            _isLoading.value = false
        }
    }
}
