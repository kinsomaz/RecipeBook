package com.samad.recipebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.samad.recipebook.databinding.ActivityHomeBinding

private lateinit var binding: ActivityHomeBinding

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.notificationRecyclerView?.layoutManager = layoutManager

        val adapter = NotificationAdapter(this, NotificationModel.Supplier.notificationModels)
        binding.notificationRecyclerView?.adapter = adapter
    }
}