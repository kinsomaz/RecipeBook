package com.samad.recipebook

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.samad.recipebook.databinding.FragmentLoginBinding
class Login : Fragment() {

    private lateinit var callbackManager : CallbackManager

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var myLifeCycleObserver: MyLifeCycleObserver
    private lateinit var startActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var view: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResult(task)

            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        binding.login.setOnClickListener {view : View ->
            val email = binding.emailInput.text.toString()
            val pass = binding.passwordInput.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        view.findNavController().navigate(R.id.action_login_to_home)

                    }else {
                        Toast.makeText(this.context, it.exception.toString(), Toast.LENGTH_SHORT).show()

                    }
                }

            } else {
                Toast.makeText(this.context, "Empty Fields Are not Allowed!! ", Toast.LENGTH_SHORT).show()
            }
        }

        binding.googleView.setOnClickListener {
            signInGoogle()
        }


        callbackManager = CallbackManager.Factory.create()


        binding.facebookView.setOnClickListener {
            loginWithFacebook()

        }

        return view
    }

    private fun loginWithFacebook() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this,callbackManager, setOf("email", "public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager, object :FacebookCallback<LoginResult> {
            override fun onCancel() {
                Toast.makeText(binding.facebookView.context, "Facebook login cancelled", Toast.LENGTH_SHORT).show()
            }


            override fun onError(error: FacebookException) {
                Toast.makeText(binding.facebookView.context, "Facebook login failed: ${error.toString()}", Toast.LENGTH_SHORT).show()
            }


            override fun onSuccess(result: LoginResult) {
                Toast.makeText(binding.facebookView.context, "Facebook login Successful", Toast.LENGTH_SHORT).show()
                Log.d("facebook", result.accessToken.token)
                handleFacebookAccessToken(result.accessToken)
            }


        })
    }


    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }else {
            Toast.makeText(this.context, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                view.findNavController().navigate(R.id.action_login_to_home)

            }else {
                Toast.makeText(this.context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityLauncher.launch(signInIntent)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.userId)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    view.findNavController().navigate(R.id.action_login_to_home)
                }

                else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", it.exception)
                    Toast.makeText(this.context, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                }
            }
    }

}
