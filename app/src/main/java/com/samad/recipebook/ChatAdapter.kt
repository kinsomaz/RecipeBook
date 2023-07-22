package com.samad.recipebook

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.samad.recipebook.databinding.ChatViewBinding


class ChatAdapter (val context: Context, private val users:ArrayList<User>): RecyclerView.Adapter<ChatAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.chat_view,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = users[position]

        holder.binding.nameChat.text = user.name
        Glide.with(context)
            .load(user.profileImage)
            .placeholder(R.drawable.image_avatar)
            .into(holder.binding.chatImage)

        holder.itemView.setOnClickListener {

            val intent = Intent(context,ChatOpenActivity::class.java)
            intent.putExtra("name",user.name)
            intent.putExtra("profileUrl",user.profileImage)
            intent.putExtra("uid",user.uid)
            context.startActivity(intent)

        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val binding : ChatViewBinding = ChatViewBinding.bind(itemView)

    }
}