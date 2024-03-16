package com.example.myapplication.model

import kotlin.math.roundToInt

data class RecipeResponse(
    val hits: List<Hit>,
    val count: Int
)

data class Hit(
    val recipe: Recipe
)

data class Recipe(
    val label: String,
    val image: String,
    val ingredients: List<Ingredient>
)

data class Ingredient(
    val text: String,
    val weight: Double
){
    override fun toString() = "$text : ${(weight * 100).roundToInt() / 100.0}gr."
}