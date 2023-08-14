package com.samad.recipebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.samad.recipebook.databinding.FragmentFriendsBinding
import java.util.Locale


class Friends : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var users: ArrayList<User>
    private lateinit var adapter: FriendAdapter
    private lateinit var searchView: SearchView
    private lateinit var user: User


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentFriendsBinding.inflate(layoutInflater)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()

        binding.backFriendsIcon.setOnClickListener {
            it.findNavController().navigate(R.id.action_friends_to_home)
        }
        binding.logoChat.setOnClickListener {
            it.findNavController().navigate(R.id.action_friends_to_home)
        }

        database = FirebaseDatabase.getInstance()
        searchView = view.findViewById(R.id.searchViewFriends)

        users = ArrayList()

        adapter = FriendAdapter(this.requireContext(), users)
        binding.friendsRecyclerView.layoutManager = GridLayoutManager(this.context, 1)

        database.reference.child("user")
            .child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        user = snapshot.getValue(User::class.java)!!
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        binding.friendsRecyclerView.adapter = adapter

        database.reference.child("user")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    users.clear()
                    for (snapshot1 in snapshot.children) {
                        if (snapshot1.value != null) {
                            binding.usersOnRecipeBook.isVisible = false
                            val user: User? = snapshot1.getValue(User::class.java)
                            if (!user!!.uid.equals(firebaseAuth.uid)) users.add(user)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)

                return true
            }
        })

        return view

    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<User>()
            for (i in users) {
                if (i.name!!.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this.context, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

}