package com.samad.recipebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.samad.recipebook.databinding.FragmentRecipeBinding

class RecipePage : Fragment() {

    private lateinit var binding: FragmentRecipeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentRecipeBinding.inflate(layoutInflater)
        val view = binding.root

        setUpRecyclerView()

        return view

    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(binding.firstRecyclerView.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.firstRecyclerView.layoutManager = layoutManager

        val adapter = RecipeAdapter(binding.firstRecyclerView.context,Recipe.Supplier.recipes)
        binding.firstRecyclerView.adapter = adapter
    }
}