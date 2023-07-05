package com.samad.recipebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.samad.recipebook.databinding.ActivityFriendsBinding

private lateinit var binding: ActivityFriendsBinding

class Friends : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.friendsRecyclerView.layoutManager = layoutManager

        val adapter = FriendAdapter(this, Friend.Supplier.friends)
        binding.friendsRecyclerView.adapter = adapter
    }
}