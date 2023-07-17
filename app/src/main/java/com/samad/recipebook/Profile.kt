package com.samad.recipebook

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.samad.recipebook.databinding.FragmentProfileBinding
import java.util.Date

class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var startActivityLauncher: ActivityResultLauncher<String>
    private var selectedImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivityLauncher = registerForActivityResult(ActivityResultContracts.GetContent())
        { uri: Uri? ->
            handleTask(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentProfileBinding.inflate(layoutInflater)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        binding.edit.setOnClickListener {
            binding.profileName.isVisible = false
            binding.nameEditView.isVisible = true
            binding.saveButton.isVisible = true
            binding.edit.isVisible = false
        }

        binding.profileImage.setOnClickListener {
            startActivityLauncher.launch("image/**")
        }

        binding.saveButton.setOnClickListener {
            val name: String = binding.nameEdit.text.toString()
            if (name.isNotEmpty()) {
                if (selectedImage != null) {
                    val reference = storage.reference
                        .child("profile")
                        .child(firebaseAuth.currentUser!!.uid)
                    reference.putFile(selectedImage!!).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            reference.downloadUrl.addOnCompleteListener { uri ->
                                val imageUrl = uri.toString()
                                val uid = firebaseAuth.currentUser!!.uid
                                val name: String = binding.nameEdit.text.toString()
                                val user = User(uid, name, imageUrl)
                                database.reference
                                    .child("user")
                                    .child(uid!!)
                                    .setValue(user)
                                    .addOnCompleteListener {

                                    }
                            }
                        } else {
                            val uid = firebaseAuth.currentUser!!.uid
                            val name: String = binding.nameEdit.text.toString()
                            val user = User(uid, name, "No image")
                            database.reference
                                .child("User")
                                .child(uid!!)
                                .setValue(user)
                                .addOnCompleteListener {

                                }

                        }
                    }

                    val uid = firebaseAuth.currentUser?.uid
                    val storageReference = storage.reference.child("profile").child("$uid")
                    storageReference.downloadUrl.addOnCompleteListener {Uri ->
                        if (Uri.isSuccessful) {
                            val imageView = binding.profileImage
                            Glide.with(this)
                                .load(Uri.result)
                                .into(imageView)

                        }
                    }

                } else {
                    binding.saveButton.isVisible = true
                    Toast.makeText(
                        this.context,
                        "please select a profile Image",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                binding.saveButton.isVisible = true
                Toast.makeText(this.context, "Please type a name", Toast.LENGTH_SHORT).show()
            }

            if (name.isNotEmpty() && selectedImage != null){
                binding.nameEditView.isVisible = false
                binding.saveButton.isVisible = false
                binding.profileName.isVisible = true
            }

        }

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = firebaseAuth.currentUser?.uid
        val userNameRef = database.getReference("user/$uid/name")
        userNameRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userName = snapshot.getValue<String>()
                if(userName != null){
                    binding.profileName.text = userName
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("The read failed: " + error.code);
            }

        })
        val storageReference = storage.reference.child("profile").child("$uid")
        storageReference.downloadUrl.addOnCompleteListener {Uri ->
            if (Uri.isSuccessful) {
                val imageView = binding.profileImage
                Glide.with(this)
                    .load(Uri.result)
                    .into(imageView)

            }
        }

    }

    private fun handleTask(task: Uri?) {
        if (task != null) {
            val uri = task //filepath
            val storage = FirebaseStorage.getInstance()
            val time = Date().time
            val reference = storage.reference
                .child("profile")
                .child(time.toString() + "")
            reference.putFile(uri!!).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reference.downloadUrl.addOnCompleteListener { uri ->
                        val filePath = uri.toString()
                        val obj = HashMap<String, Any>()
                        obj["profileImage"] = filePath
                        database.reference
                            .child("user")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .updateChildren(obj)
                            .addOnCompleteListener {

                            }
                    }
                }
            }
            binding.profileImage.setImageURI(uri)
            selectedImage = uri
        }
    }

}