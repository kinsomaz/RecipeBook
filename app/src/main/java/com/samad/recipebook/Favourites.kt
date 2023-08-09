package com.samad.recipebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.samad.recipebook.databinding.FragmentFavouritesBinding

class Favourites : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var layoutManager: FlexboxLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var adapter: FavouriteAdapter
    private lateinit var recipeList : ArrayList<RecipeData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentFavouritesBinding.inflate(layoutInflater)
        val view = binding.root

        binding.backFavouritesIcon.setOnClickListener {
            view.findNavController().navigate(R.id.action_favourites_to_home)
        }

        recyclerView = view.findViewById(R.id.favourite_recycler_view)

        layoutManager = FlexboxLayoutManager(this.context)
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.FLEX_START

        recipeList = ArrayList<RecipeData>()

        recyclerView.layoutManager = layoutManager
        adapter = FavouriteAdapter(recipeList)
        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        database.reference.child("userFavouriteRecipes")
            .child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    recipeList.clear()
                    for (snapshot1 in snapshot.children){
                        val recipe = snapshot1.getValue(RecipeData::class.java)
                        recipeList.add(recipe!!)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        return view

    }
}