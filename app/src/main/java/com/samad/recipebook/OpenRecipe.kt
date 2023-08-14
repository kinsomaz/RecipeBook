package com.samad.recipebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.samad.recipebook.databinding.FragmentOpenRecipeBinding

class OpenRecipe : Fragment() {

    private lateinit var binding: FragmentOpenRecipeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentOpenRecipeBinding.inflate(layoutInflater)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        val name = arguments?.getString("RecipeName")
        binding.title.text = name

        binding.back.setOnClickListener {
            view.findNavController().navigate(R.id.action_openRecipe_to_recipePageContainer)
        }

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
        return view
    }

}
