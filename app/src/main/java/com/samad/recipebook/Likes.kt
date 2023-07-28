package com.samad.recipebook

data class Likes(var title: String) {
    object Supplier {
        val likes = listOf<Likes>(
            Likes("Yam"),
            Likes("Ground Nut"),
            Likes("Sausage"),
            Likes("Exercise"),
            Likes("Giving"),
            Likes("Artificial Intelligence")
        )
    }
}
