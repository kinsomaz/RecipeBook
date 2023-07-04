package com.samad.recipebook

data class Recipe(var title: String) {

    object Supplier{
        val recipes = listOf<Recipe>(
            Recipe("My Search History"),
            Recipe("My Favourite Recipes"),
            Recipe("Easy Mexican Casserole"),
            Recipe("Thai Chicken Balls"),
            Recipe("Honey Mustard Pork Chops"),
            Recipe("Salsa Chicken Rice Casserole"),
            Recipe("Banana Cinnamon Roll Casserole ")
        )
    }
}