package com.samad.recipebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.samad.recipebook.databinding.ChatViewBinding
import com.squareup.picasso.Picasso

private lateinit var binding: ChatViewBinding

class ChatAdapter (val context: Context, private val chats:List<Chat>): RecyclerView.Adapter<ChatAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ChatViewBinding.inflate(LayoutInflater.from(context), parent ,false)
        val view = binding.root
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chat = chats[position]
        holder.setData(chat, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun setData(chat: Chat?, pos: Int){
            chat?.let {
                binding.nameChat.text = chat.name
                binding.chatMessage.text = chat.latestMessage
                Picasso.with(context).load(chat.image).into(binding.chatImage)
            }
        }

    }
}