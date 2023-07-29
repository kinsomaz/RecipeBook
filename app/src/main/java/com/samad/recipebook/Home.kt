package com.samad.recipebook


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var notifications: ArrayList<Notification>
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        view = binding.root

        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.uid?.let {
            database.reference.child("user").child(it)
                .child("profileImage").addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val imageView = binding.avatar
                        if (snapshot.exists()) {
                            Glide.with(this@Home)
                                .load(snapshot.value)
                                .placeholder(R.drawable.image_avatar)
                                .into(imageView)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

        database.reference.child("user")
            .child(firebaseAuth.uid!!)
            .child("name")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        binding.userName?.text = snapshot.value as CharSequence?
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        notifications = ArrayList()
        recyclerView = view.findViewById(R.id.notificationRecyclerView)

        adapter = NotificationAdapter(this.requireContext(), notifications)
        val layoutManager = GridLayoutManager(this.context, 1)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        database.reference.child("userNotification")
            .child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    notifications.clear()
                    for (snapshot1 in snapshot.children) {
                        val notification = snapshot1.getValue(Notification::class.java)
                        if (notification != null) {
                            notifications.add(notification)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }


                override fun onCancelled(error: DatabaseError) {}
            })

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val notification = notifications[position]
                val notificationTime = notification.timestamp
                database.reference.child("userNotification").child(firebaseAuth.uid!!).addValueEventListener(object : ValueEventListener{

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snapshot1 in snapshot.children) {
                            val not = snapshot1.getValue(Notification::class.java)
                            if(not?.timestamp == notificationTime){
                                val key = snapshot1.key
                                database.reference.child("userNotification").child(firebaseAuth.uid!!).child(key!!).removeValue()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
                recyclerView.adapter?.notifyItemRemoved(position)
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        binding.avatar.setOnClickListener {
            view.findNavController().navigate(R.id.action_home_to_profile)
        }
        binding.card1.setOnClickListener {
            view.findNavController().navigate(R.id.action_home_to_recipePageContainer)
        }
        binding.card2.setOnClickListener {
            view.findNavController().navigate(R.id.action_home_to_chats)
        }
        binding.card4.setOnClickListener {
            view.findNavController().navigate(R.id.action_home_to_friends)
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

}