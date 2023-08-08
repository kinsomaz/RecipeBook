package com.samad.recipebook

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.ArrayList

class NetworkAdapter( var recipeList: List<RecipeData>) : RecyclerView.Adapter<NetworkAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_view, parent, false)
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

    fun setFilteredList(filteredList: ArrayList<RecipeData>) {
        this.recipeList = filteredList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val bitmap : ImageView = itemView.findViewById(R.id.bitmapImage)
        val title : TextView = itemView.findViewById(R.id.first_recipe_title)
    }
}