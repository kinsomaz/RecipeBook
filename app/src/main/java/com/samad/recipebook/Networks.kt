package com.samad.recipebook


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
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
import com.samad.recipebook.databinding.FragmentNetworksBinding
import java.util.Locale

class Networks : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentNetworksBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: NetworkAdapter
    private lateinit var layoutManager: FlexboxLayoutManager
    private lateinit var database: FirebaseDatabase
    private lateinit var recipeList : ArrayList<RecipeData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentNetworksBinding.inflate(layoutInflater)
        val view = binding.root

        binding.backNetworkIcon.setOnClickListener {
            view.findNavController().navigate(R.id.action_networks_to_home)
        }

        recyclerView = view.findViewById(R.id.networkRecyclerView)
        searchView = view.findViewById(R.id.searchViewNetwork)

        layoutManager = FlexboxLayoutManager(this.context)
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.FLEX_START

        recipeList = ArrayList<RecipeData>()

        recyclerView.layoutManager = layoutManager
        adapter = NetworkAdapter(recipeList)
        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        database.reference.child("userRecipeNetwork")
            .child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener{

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

        searchView.setOnQueryTextListener(object : OnQueryTextListener{

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
        if(query != null){
            val filteredList = ArrayList<RecipeData>()
            for(i in recipeList){
                if (i.title!!.lowercase(Locale.ROOT).contains(query)){
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()){
                Toast.makeText(this.context,"No Data found", Toast.LENGTH_SHORT).show()
            }
            else{
                adapter.setFilteredList(filteredList)
            }
        }
    }

}