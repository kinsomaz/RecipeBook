package com.samad.recipebook


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.samad.recipebook.databinding.FragmentHomeBinding


class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var view: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        view = binding.root

        setUpRecyclerView()

        binding.avatar.setOnClickListener {
           view.findNavController().navigate(R.id.action_home_to_profile)
        }

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