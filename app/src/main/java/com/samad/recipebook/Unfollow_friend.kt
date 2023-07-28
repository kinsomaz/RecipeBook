package com.samad.recipebook

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.samad.recipebook.databinding.ActivityUnfollowFriendBinding

class Unfollow_friend : AppCompatActivity() {

    private lateinit var binding: ActivityUnfollowFriendBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var adapter: LikesAdapter
    private lateinit var layoutManager: FlexboxLayoutManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnfollowFriendBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val name = intent.getStringExtra("name")
        val profile = intent.getStringExtra("profileUrl")
        val uid = intent.getStringExtra("uid")

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        binding.name.text = name
        Glide.with(this).load(profile)
            .placeholder(R.drawable.image_avatar)
            .into(binding.image)

        database.reference.child("userFriend")
            .child(uid!!)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val friends = snapshot.childrenCount
                        binding.friendsCount.text = "$friends"


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        recyclerView = view.findViewById(R.id.likeRecyclerView)

        layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        layoutManager.alignItems = AlignItems.FLEX_START

        recyclerView.layoutManager = layoutManager
        adapter = LikesAdapter(Likes.Supplier.likes)
        recyclerView.adapter = adapter

        binding.back.setOnClickListener {}

        binding.removeFriendButton.setOnClickListener {
            database.reference.child("userFriend")
                .child(firebaseAuth.uid!!)
                .child(uid).addValueEventListener(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        database.reference.child("userFriend").child(firebaseAuth.uid!!).child(uid).removeValue()
                        binding.removeFriendButton.isVisible = false
                        binding.addFriendButton.isVisible = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        database.reference.child("userFriend").child(firebaseAuth.uid!!).child(uid).addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()){
                    binding.addFriendButton.isVisible = true

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.addFriendButton.setOnClickListener {
            database.reference.child("userFriend")
                .child(firebaseAuth.uid!!)
                .child(uid)
                .setValue(User(uid,name,profile))
                .addOnCompleteListener {
                    binding.addFriendButton.isVisible = false
                    binding.removeFriendButton.isVisible = true
                }
        }

    }

}