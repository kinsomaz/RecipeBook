package com.samad.recipebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.samad.recipebook.databinding.ActivityOpenRecipeBinding

class OpenRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityOpenRecipeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenRecipeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val name = intent.getStringExtra("RecipeName")
        binding.title.text = name

    }

}
