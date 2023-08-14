package com.samad.recipebook


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
import com.samad.recipebook.databinding.FragmentUnfollowFriendBinding

class Unfollow_friend : Fragment() {

    private lateinit var binding: FragmentUnfollowFriendBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adapter: LikesAdapter
    private lateinit var layoutManager: FlexboxLayoutManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentUnfollowFriendBinding.inflate(layoutInflater)
        val view = binding.root

        val name = arguments?.getString("name")
        val profile = arguments?.getString("profileUrl")
        val uid = arguments?.getString("uid")

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

                override fun onCancelled(error: DatabaseError) {}
            })

        database.reference.child("userRecipeNetwork")
            .child(uid!!)
            .addValueEventListener(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val network = snapshot.childrenCount
                        binding.networksCount.text = "$network"
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })

        recyclerView = view.findViewById(R.id.likeRecyclerView)

        layoutManager = FlexboxLayoutManager(this.requireContext())
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        layoutManager.alignItems = AlignItems.FLEX_START

        recyclerView.layoutManager = layoutManager
        adapter = LikesAdapter(Likes.Supplier.likes)
        recyclerView.adapter = adapter

        binding.back.setOnClickListener {
            view.findNavController().navigate(R.id.action_unfollow_friend_to_friends)
        }

        binding.removeFriendButton.setOnClickListener {
            database.reference.child("userFriend")
                .child(firebaseAuth.uid!!)
                .child(uid).addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            database.reference.child("userFriend").child(firebaseAuth.uid!!)
                                .child(uid).removeValue()
                            binding.removeFriendButton.isVisible = false
                            binding.addFriendButton.isVisible = true
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

        binding.addFriendButton.setOnClickListener {
            database.reference.child("userFriend")
                .child(firebaseAuth.uid!!)
                .child(uid)
                .setValue(User(uid, name, profile))
                .addOnCompleteListener {
                    binding.addFriendButton.isVisible = false
                    binding.removeFriendButton.isVisible = true
                }
        }
        return view

    }

}