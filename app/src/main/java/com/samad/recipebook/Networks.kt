package com.samad.recipebook


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.samad.recipebook.databinding.FragmentNetworksBinding

class Networks : Fragment() {

    private lateinit var binding: FragmentNetworksBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentNetworksBinding.inflate(layoutInflater)
        val view = binding.root

        return view

    }
}