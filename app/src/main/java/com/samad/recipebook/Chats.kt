package com.samad.recipebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.samad.recipebook.databinding.ActivityChatsBinding

private lateinit var binding: ActivityChatsBinding

class Chats : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.chatRecyclerView.layoutManager = layoutManager

        val adapter = ChatAdapter(this, Chat.Supplier.chats)
        binding.chatRecyclerView.adapter = adapter
    }
}