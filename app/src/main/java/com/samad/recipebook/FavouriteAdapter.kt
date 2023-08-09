package com.samad.recipebook

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavouriteAdapter(var recipeList: List<RecipeData>) : RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_view_favourite,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = recipeList[position].title
        Glide.with(holder.itemView.context)
            .load(recipeList[position].bitmap)
            .placeholder(R.drawable.bitmap1)
            .into(holder.bitmap)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, OpenRecipe::class.java)
            intent.putExtra("RecipeName",recipeList[position].title)
            holder.itemView.context.startActivity(intent)
        }
    }

    inner class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val bitmap = itemView.findViewById<ImageView>(R.id.bitmapImage_favourite)
        val title = itemView.findViewById<TextView>(R.id.first_recipe_title_favourite)
    }
}