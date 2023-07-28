package com.samad.recipebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.samad.recipebook.databinding.ItemViewBinding

private lateinit var binding: ItemViewBinding

class RecipePageAdapter(val context: Context, private val recipes: List<Recipe>): RecyclerView.Adapter<RecipePageAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemViewBinding.inflate(LayoutInflater.from(context),parent,false)
        val view = binding.root
        return MyViewHolder(view)
    }


    override fun getItemCount(): Int {
        return recipes.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.setData(recipe,position)
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun setData(recipe: Recipe?,pos: Int){
            recipe?.let {
                binding.itemTitle.text = recipe.title
            }
        }
    }
}