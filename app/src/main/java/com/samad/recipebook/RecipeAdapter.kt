package com.samad.recipebook

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeAdapter( var recipeList: List<RecipeData>) : RecyclerView.Adapter<RecipeAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_view, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bitmap.setImageResource(recipeList[position].bitmap)
        holder.title.text = recipeList[position].title
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, OpenRecipe::class.java)
            intent.putExtra("RecipeName",recipeList[position].title)
            holder.itemView.context.startActivity(intent)
        }
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val bitmap : ImageView = itemView.findViewById(R.id.bitmap1)
        val title : TextView = itemView.findViewById(R.id.first_recipe_title)
    }

    fun setFilteredList(recipeList: List<RecipeData>){
        this.recipeList = recipeList
        notifyDataSetChanged()
    }

}