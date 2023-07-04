package com.samad.recipebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.samad.recipebook.databinding.ActivityRecipeBinding
import com.samad.recipebook.databinding.ActivityRecipeIiBinding

private lateinit var binding: ActivityRecipeIiBinding

class RecipePage_II : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeIiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.firstRecyclerViewII.layoutManager = layoutManager

        val adapter = RecipeAdapter(this,Recipe.Supplier.recipes)
        binding.firstRecyclerViewII.adapter = adapter
    }
}