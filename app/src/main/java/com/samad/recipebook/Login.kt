package com.samad.recipebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.samad.recipebook.databinding.FragmentLoginBinding

class Login : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var myLifeCycleObserver: MyLifeCycleObserver
    private lateinit var startActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var view: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleResult(task)

                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        view = binding.root

        myLifeCycleObserver = MyLifeCycleObserver(this.lifecycle, binding.relativeLayout.context)
        lifecycle.addObserver(myLifeCycleObserver)

        firebaseAuth = FirebaseAuth.getInstance()

        val gsa = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(binding.relativeLayout.context, gsa)

        binding.createButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_login_to_signUp)
        }

        binding.login.setOnClickListener { view: View ->
            val email = binding.emailInput.text.toString()
            val pass = binding.passwordInput.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        view.findNavController().navigate(R.id.action_login_to_home)

                    } else {
                        Toast.makeText(this.context, it.exception.toString(), Toast.LENGTH_SHORT)
                            .show()

                    }
                }

            } else {
                Toast.makeText(this.context, "Empty Fields Are not Allowed!! ", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.googleView.setOnClickListener {
            signInGoogle()
        }


        return view
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null){
           view.findNavController().navigate(R.id.action_login_to_home)
        }

    }


    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(this.context, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                view.findNavController().navigate(R.id.action_login_to_home)

            } else {
                Toast.makeText(this.context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityLauncher.launch(signInIntent)
    }


}
