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
import com.samad.recipebook.databinding.FriendViewBinding

class FriendAdapter(val context: Context, private var users: ArrayList<User>) :
    RecyclerView.Adapter<FriendAdapter.MyViewHolder>() {

    private lateinit var database: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.friend_view, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = users[position]
        holder.setData(user, position)

        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        database.reference.child("userFriend")
            .child(user.uid!!)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val friends = snapshot.childrenCount
                        holder.binding.friendsNum.text = "$friends"

                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        holder.binding.addButton?.setOnClickListener {
            database.reference.child("userFriend")
                .child(firebaseAuth.uid!!)
                .child(user.uid!!)
                .setValue(user)
                .addOnCompleteListener {
                    holder.binding.addFriend.visibility = View.GONE
                    holder.binding.added.visibility = View.VISIBLE
                }

        }

        database.reference.child("userFriend")
            .child(firebaseAuth.uid!!)
            .child(user.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        holder.binding.addFriend.visibility = View.GONE
                        holder.binding.added.visibility = View.VISIBLE
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        holder.binding.friendImage.setOnClickListener {
            val bundle = Bundle().apply {
                putString("name",user.name)
                putString("profileUrl",user.profileImage)
                putString("uid",user.uid)
            }
            holder.itemView.findNavController().navigate(R.id.action_friends_to_unfollow_friend,bundle)
        }

    }


    fun setFilteredList(filteredList: ArrayList<User>) {
        this.users = filteredList
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = FriendViewBinding.bind(itemView)
        fun setData(user: User?, pos: Int) {
            user?.let {
                binding.friendName.text = user.name
                Glide.with(context)
                    .load(user.profileImage)
                    .placeholder(R.drawable.image_avatar)
                    .into(binding.friendImage)
            }
        }

    }


}