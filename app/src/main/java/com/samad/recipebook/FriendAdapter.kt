package com.samad.recipebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.samad.recipebook.databinding.FriendViewBinding
import com.squareup.picasso.Picasso

private lateinit var binding: FriendViewBinding

class FriendAdapter(val context: Context, private val friends: List<Friend>): RecyclerView.Adapter<FriendAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = FriendViewBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val friend = friends[position]
        holder.setData(friend, position)
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun setData(friend: Friend?, pos: Int) {
            friend?.let {
                binding.friendName.text = friend.name
                binding.friendsNum.text = friend.follower
                Picasso.with(context).load(friend.image).into(binding.friendImage)
            }
        }

    }
}