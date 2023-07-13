package com.samad.recipebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.samad.recipebook.databinding.FragmentChatsBinding

class Chats : Fragment() {

    private lateinit var binding: FragmentChatsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentChatsBinding.inflate(layoutInflater)
        val view = binding.root

        setUpRecyclerView()

        return view
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(binding.chatRecyclerView.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.chatRecyclerView.layoutManager = layoutManager

        val adapter = ChatAdapter(binding.chatRecyclerView.context, Chat.Supplier.chats)
        binding.chatRecyclerView.adapter = adapter
    }

}