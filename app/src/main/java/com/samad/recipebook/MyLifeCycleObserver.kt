package com.samad.recipebook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.auth.FirebaseAuth


class MyLifeCycleObserver(lifecycle: Lifecycle, var context: Context): DefaultLifecycleObserver {

    private lateinit var firebaseAuth: FirebaseAuth

}