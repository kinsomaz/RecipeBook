package com.samad.recipebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.samad.recipebook.databinding.FragmentFriendsBinding


class Friends : Fragment() {

    private lateinit var binding: FragmentFriendsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentFriendsBinding.inflate(layoutInflater)
        val view = binding.root

        setUpRecyclerView()

        return view

    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(binding.friendsRecyclerView.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.friendsRecyclerView.layoutManager = layoutManager

        val adapter = FriendAdapter(binding.friendsRecyclerView.context, Friend.Supplier.friends)
        binding.friendsRecyclerView.adapter = adapter
    }
}