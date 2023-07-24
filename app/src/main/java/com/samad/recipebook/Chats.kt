package com.samad.recipebook

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.samad.recipebook.databinding.FragmentChatsBinding

class Chats : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var users: ArrayList<User>
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var dialog: ProgressDialog
    private lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentChatsBinding.inflate(layoutInflater)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()

        dialog = ProgressDialog(this.context)
        dialog.setMessage("Uploading Image...")
        dialog.setCancelable(false)

        binding.backChatsIcon.setOnClickListener {
            it.findNavController().navigate(R.id.action_chats_to_home)
        }

        binding.logoChat.setOnClickListener {
            it.findNavController().navigate(R.id.action_chats_to_home)
        }

        database = FirebaseDatabase.getInstance()

        users = ArrayList<User>()

        chatAdapter = ChatAdapter(this.requireContext(),users)
        val layoutManager = GridLayoutManager(this.context,1)
        binding.chatRecyclerView.layoutManager = layoutManager



        binding.chatRecyclerView.adapter = chatAdapter

        database.reference.child("userFriend")
            .child(firebaseAuth.uid!!)
            .addValueEventListener(object :ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    users.clear()
                    for (snapshot1 in snapshot.children){
                        val user: User? = snapshot1.getValue(User::class.java)
                        if (!user!!.uid.equals(firebaseAuth.uid)) users.add(user)
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            } )

        return view
    }

    override fun onResume() {
        super.onResume()
        val currentId = firebaseAuth.uid
        database.reference.child("presence")
            .child(currentId!!)
            .setValue("Online")
    }

    override fun onPause() {
        super.onPause()
        val currentId = firebaseAuth.uid
        database.reference.child("presence")
            .child(currentId!!)
            .setValue("Offline")

    }

}