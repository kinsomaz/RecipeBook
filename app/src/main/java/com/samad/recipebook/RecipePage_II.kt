package com.samad.recipebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.samad.recipebook.databinding.FragmentRecipeIiBinding

class RecipePage_II : Fragment() {

    private lateinit var binding: FragmentRecipeIiBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentRecipeIiBinding.inflate(layoutInflater)
        val view = binding.root

        setUpRecyclerView()

        return view

    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(binding.firstRecyclerViewII.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.firstRecyclerViewII.layoutManager = layoutManager

        val adapter = RecipeAdapter(binding.firstRecyclerViewII.context,Recipe.Supplier.recipes)
        binding.firstRecyclerViewII.adapter = adapter
    }
}