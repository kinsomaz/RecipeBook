package com.samad.recipebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.samad.recipebook.databinding.ActivityRecipeBinding

private lateinit var binding: ActivityRecipeBinding

class RecipePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.firstRecyclerView.layoutManager = layoutManager

        val adapter = RecipeAdapter(this,Recipe.Supplier.recipes)
        binding.firstRecyclerView.adapter = adapter
    }
}