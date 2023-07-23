package com.samad.recipebook


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.samad.recipebook.databinding.FragmentHomeBinding


class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var view: View
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        view = binding.root

        database = FirebaseDatabase.getInstance()
        FirebaseAuth.getInstance().uid?.let {
            database.reference.child("user").child(it)
                .child("profileImage").addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val imageView = binding.avatar
                        if(snapshot.exists()){
                        Glide.with(this@Home)
                            .load(snapshot.value)
                            .placeholder(R.drawable.image_avatar)
                            .into(imageView)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

        setUpRecyclerView()



        binding.avatar.setOnClickListener {
            view.findNavController().navigate(R.id.action_home_to_profile)
        }
        binding.card2.setOnClickListener {
            view.findNavController().navigate(R.id.action_home_to_chats)
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currentId!!)
            .setValue("Online")
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currentId!!)
            .setValue("Offline")
    }

    private fun setUpRecyclerView(){
        val layoutManager = LinearLayoutManager(binding.notificationRecyclerView?.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.notificationRecyclerView?.layoutManager = layoutManager

        val adapter = context?.let { NotificationAdapter(it, NotificationModel.Supplier.notificationModels) }
        binding.notificationRecyclerView?.adapter = adapter
    }
}