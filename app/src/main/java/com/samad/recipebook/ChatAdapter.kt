package com.samad.recipebook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.samad.recipebook.databinding.ChatViewBinding


class ChatAdapter (val context: Context, private val users:ArrayList<User>): RecyclerView.Adapter<ChatAdapter.MyViewHolder>(){

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_view,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = users[position]
        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.uid

        holder.binding.nameChat.text = user.name
        Glide.with(context)
            .load(user.profileImage)
            .placeholder(R.drawable.image_avatar)
            .into(holder.binding.chatImage)

        database.reference.child("chats")
            .child(uid + user.uid)
            .child("lastMsg")
            .addValueEventListener(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        val lastMsg = snapshot.value
                        holder.binding.chatMessage.text = lastMsg as CharSequence?
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        holder.itemView.setOnClickListener {

            val bundle = Bundle().apply {
                putString("name",user.name)
                putString("profileUrl",user.profileImage)
                putString("uid",user.uid)
            }
            holder.itemView.findNavController().navigate(R.id.action_chats_to_chatOpen,bundle)


        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val binding : ChatViewBinding = ChatViewBinding.bind(itemView)

    }


}