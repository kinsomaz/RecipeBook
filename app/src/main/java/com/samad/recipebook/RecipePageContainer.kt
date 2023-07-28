package com.samad.recipebook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.samad.recipebook.databinding.FragmentRecipePageContainerBinding


class RecipePageContainer : Fragment() {

    private lateinit var binding: FragmentRecipePageContainerBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: RecipePageContainerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentRecipePageContainerBinding.inflate(layoutInflater)
        val view = binding.root

        viewPager2 = view.findViewById(R.id.viewPager2)
        adapter = RecipePageContainerAdapter(parentFragmentManager,lifecycle)
        viewPager2.adapter = adapter


        return view
    }


}