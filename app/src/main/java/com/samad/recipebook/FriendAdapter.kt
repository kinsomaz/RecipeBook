package com.samad.recipebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.samad.recipebook.databinding.FriendViewBinding
import com.squareup.picasso.Picasso



class FriendAdapter(val context: Context, private val users: ArrayList<User>): RecyclerView.Adapter<FriendAdapter.MyViewHolder>() {

    private lateinit var database: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.friend_view,parent,false)
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
            .addValueEventListener(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        val friends = snapshot.childrenCount
                        holder.binding.friendsNum.text = "$friends"


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
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
            .child("uid")
            .addValueEventListener(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    val uid = snapshot.value
                    if (uid != null) {
                        holder.binding.addFriend.visibility = View.GONE
                        holder.binding.added.visibility = View.VISIBLE
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = FriendViewBinding.bind(itemView)
        fun setData(user: User?, pos: Int) {
            user?.let {
                binding.friendName.text = user.name
                Picasso.with(context).load(user.profileImage).into(binding.friendImage)
            }
        }

    }
}