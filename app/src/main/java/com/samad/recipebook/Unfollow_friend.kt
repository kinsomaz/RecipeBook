package com.samad.recipebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.samad.recipebook.databinding.FragmentUnfollowFriendBinding

class Unfollow_friend : Fragment() {

    private lateinit var binding: FragmentUnfollowFriendBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentUnfollowFriendBinding.inflate(layoutInflater)
        val view = binding.root

        return view

    }
}