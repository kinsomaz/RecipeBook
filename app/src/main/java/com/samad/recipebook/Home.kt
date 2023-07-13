package com.samad.recipebook


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.samad.recipebook.databinding.FragmentHomeBinding


class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root

        setUpRecyclerView()

        return view
    }

    private fun setUpRecyclerView(){
        val layoutManager = LinearLayoutManager(binding.notificationRecyclerView?.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.notificationRecyclerView?.layoutManager = layoutManager

        val adapter = NotificationAdapter(binding.notificationRecyclerView!!.context, NotificationModel.Supplier.notificationModels)
        binding.notificationRecyclerView?.adapter = adapter
    }
}