package com.samad.recipebook

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.samad.recipebook.databinding.ActivityOpenRecipeBinding

class OpenRecipe : AppCompatActivity() {

    private lateinit var binding: ActivityOpenRecipeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenRecipeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        val name = intent.getStringExtra("RecipeName")
        binding.title.text = name

        val reference = storage.reference
            .child("userRecipeNetwork")
            .child(name!!)

        binding.iconBackground.setOnClickListener {
            reference.downloadUrl.addOnSuccessListener { Uri ->
                val recipes = HashMap<String, Any>()
                recipes["title"] = name
                recipes["bitmap"] = Uri.toString()
                database.reference.child("userFavouriteRecipes")
                    .child(firebaseAuth.uid!!)
                    .child(name)
                    .setValue(recipes)
                    .addOnCompleteListener {
                        binding.iconBackground.setImageResource(R.drawable.love_red)
                    }
            }
        }

        database.reference.child("userFavouriteRecipes")
            .child(firebaseAuth.uid!!)
            .child(name)
            .addValueEventListener(object  : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        binding.iconBackground.setImageResource(R.drawable.love_red)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

    }
}
