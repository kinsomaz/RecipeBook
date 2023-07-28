package com.samad.recipebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class LikesAdapter(var likes: List<Likes>) : RecyclerView.Adapter<LikesAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.likes_view,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return likes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.button.text = likes[position].title
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val button : TextView = itemView.findViewById(R.id.like)

    }
}