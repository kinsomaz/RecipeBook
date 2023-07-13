package com.samad.recipebook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.samad.recipebook.databinding.FragmentSignUpBinding

class SignUp : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        binding = FragmentSignUpBinding.inflate(layoutInflater)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            it.findNavController().navigate(R.id.action_signUp_to_login)

        }

        binding.button.setOnClickListener {view: View ->
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this.context, "Account created Successfully. \n You need to Login", Toast.LENGTH_SHORT).show()
                            view.findNavController().navigate(R.id.action_signUp_to_login)

                        } else {
                            Toast.makeText(this.context, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }

                } else {
                    Toast.makeText(this.context, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this.context, "Empty Fields Are not Allowed!! ", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}