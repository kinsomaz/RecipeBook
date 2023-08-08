package com.samad.recipebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.samad.recipebook.databinding.FragmentRecipeBinding
import java.util.Locale

class RecipePage : Fragment() {

    private lateinit var binding: FragmentRecipeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var recipeList = ArrayList<RecipeData>()
    private lateinit var adapter: RecipeAdapter
    private lateinit var layoutManager: FlexboxLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentRecipeBinding.inflate(layoutInflater)
        val view = binding.root

        binding.back.setOnClickListener {
            view.findNavController().navigate(R.id.action_recipePageContainer_to_home)
        }
        binding.explore.setOnClickListener {
            binding.secondRecyclerView.visibility = View.GONE
            binding.exploreContainer.visibility = View.GONE
            binding.scrollView.visibility = View.VISIBLE
        }

        recyclerView = view.findViewById(R.id.recipeRecyclerView)
        searchView = view.findViewById(R.id.searchView)



        layoutManager = FlexboxLayoutManager(this.context)
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.FLEX_START

        addDataToList()
        recyclerView.layoutManager = layoutManager
        adapter = RecipeAdapter(recipeList)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                binding.secondRecyclerView.visibility = View.GONE
                binding.exploreContainer.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE
                return true
            }
        })

        setUpRecyclerView()

        return view

    }
    private fun filterList(query : String?){
        if(query != null){
            val filteredList = ArrayList<RecipeData>()
            for(i in recipeList){
                if (i.title!!.lowercase(Locale.ROOT).contains(query)){
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()){
                Toast.makeText(this.context,"No Data found", Toast.LENGTH_SHORT).show()
            }
            else{
                adapter.setFilteredList(filteredList)
            }
        }
    }

    private fun addDataToList() {
        recipeList.add(RecipeData("Sausage & Peppers Group", R.drawable.bitmap1))
        recipeList.add(RecipeData("Pro-bros", R.drawable.bitmap2))
        recipeList.add(RecipeData("Food Fitfam", R.drawable.bitmap3))
        recipeList.add(RecipeData("Toast With Peanut Butter", R.drawable.bitmap4))
        recipeList.add(RecipeData("Strawberry Wonders", R.drawable.bitmap5))
        recipeList.add(RecipeData("Colours of Strawberry", R.drawable.bitmap6))
        recipeList.add(RecipeData("Veges Gang", R.drawable.bitmap7))
        recipeList.add(RecipeData("Waffle Berries Closeup", R.drawable.bitmap8))

    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(binding.secondRecyclerView.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.secondRecyclerView.layoutManager = layoutManager

        val adapter = RecipePageAdapter(binding.secondRecyclerView.context,Recipe.Supplier.recipes)
        binding.secondRecyclerView.adapter = adapter
    }
}