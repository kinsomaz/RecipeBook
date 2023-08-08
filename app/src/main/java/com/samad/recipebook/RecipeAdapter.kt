package com.samad.recipebook

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class RecipeAdapter( var recipeList: List<RecipeData>) : RecyclerView.Adapter<RecipeAdapter.MyViewHolder>() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_view, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bitmap.setImageResource(recipeList[position].bitmap as Int)
        holder.title.text = recipeList[position].title
        holder.itemView.setOnClickListener {

            database = FirebaseDatabase.getInstance()
            storage = FirebaseStorage.getInstance()
            firebaseAuth = FirebaseAuth.getInstance()

            val reference = storage.reference
                .child("userRecipeNetwork")
                .child(recipeList[position].title!!)
            val imageUri = Uri.parse("android.resource://" + holder.itemView.context.packageName + "/" + recipeList[position].bitmap)
            reference.putFile(imageUri).addOnCompleteListener {
                if (it.isSuccessful){
                    reference.downloadUrl.addOnSuccessListener { Uri ->
                        val recipes = HashMap<String,Any>()
                        recipes["title"] = recipeList[position].title!!
                        recipes["bitmap"] = Uri.toString()
                        database.reference.child("userRecipeNetwork")
                            .child(firebaseAuth.uid!!)
                            .child(recipeList[position].title!!)
                            .setValue(recipes)
                            .addOnCompleteListener {}



                    }
                }
            }

            val intent = Intent(holder.itemView.context, OpenRecipe::class.java)
            intent.putExtra("RecipeName",recipeList[position].title)
            holder.itemView.context.startActivity(intent)
        }
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val bitmap : ImageView = itemView.findViewById(R.id.bitmapImage)
        val title : TextView = itemView.findViewById(R.id.first_recipe_title)
    }

    fun setFilteredList(recipeList: List<RecipeData>){
        this.recipeList = recipeList
        notifyDataSetChanged()
    }

}