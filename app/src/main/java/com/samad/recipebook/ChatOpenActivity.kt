package com.samad.recipebook

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.samad.recipebook.databinding.ActivityChatOpenBinding
import java.util.Calendar
import java.util.Date

class ChatOpenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatOpenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var startActivityLauncher: ActivityResultLauncher<String>

    var adapter: MessageAdapter? = null
    var messages: ArrayList<Message>? = null
    var senderRoom: String? = null
    var receiverRoom: String? = null
    var database: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    var dialog: ProgressDialog? = null
    var senderUid: String? = null
    var receiverUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatOpenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        dialog = ProgressDialog(this)
        dialog!!.setMessage("Uploading image...")
        dialog!!.setCancelable(false)
        messages = ArrayList()

        val name = intent.getStringExtra("name")
        val profile = intent.getStringExtra("profileUrl")
        binding.name.text = name
        Glide.with(this).load(profile)
            .placeholder(R.drawable.image_placeholder)
            .into(binding.profile01)

        val uid = intent.getStringExtra("uid")
        receiverUid = uid

        binding.toolbar.isVisible = true
        binding.backIcon.setOnClickListener {

        }

        startActivityLauncher = registerForActivityResult(ActivityResultContracts.GetContent())
        { result ->
            handleTask(result)

        }

        senderUid = FirebaseAuth.getInstance().uid

        database!!.reference.child("presence").child(receiverUid!!)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val status = snapshot.getValue(String::class.java)
                        if (status == "offline") {
                            binding.status.visibility = View.GONE
                        } else {
                            binding.status.text = status
                            binding.status.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid

        adapter =
            MessageAdapter(binding.firstLayout.context, messages, senderRoom!!, receiverRoom!!)

        binding.recyclerView.layoutManager = LinearLayoutManager(binding.firstLayout.context)
        binding.recyclerView.adapter = adapter

        database!!.reference.child("chats")
            .child(senderRoom!!)
            .child("message")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    messages!!.clear()
                    for (snapshot1 in snapshot.children) {
                        val message: Message? = snapshot1.getValue(Message::class.java)
                        message!!.messsageId = snapshot1.key
                        messages!!.add(message)
                    }

                    adapter!!.notifyDataSetChanged()
                }


                override fun onCancelled(error: DatabaseError) {}

            })

        binding.send.setOnClickListener {
            val messageTxt: String = binding!!.messageBox.text.toString()
            val date = Date()
            val message = Message(messageTxt, senderUid, date.time)

            database!!.reference.child("user").child(firebaseAuth.uid!!)
                .child("name")
                .addValueEventListener(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.value
                    addToHisNotifications(receiverUid!!, "$name sent you a message")

                }

                override fun onCancelled(error: DatabaseError) {}
            })


            binding.messageBox.setText("")
            val randomKey = database!!.reference.push().key
            val lastMsgObj = HashMap<String, Any>()
            lastMsgObj["lastMsg"] = message.messsage!!
            lastMsgObj["lastMsgTime"] = date.time

            database!!.reference.child("chats").child(senderRoom!!)
                .updateChildren(lastMsgObj)
            database!!.reference.child("chats").child(receiverRoom!!)
                .updateChildren(lastMsgObj)
            database!!.reference.child("chats").child(senderRoom!!)
                .child("message")
                .child(randomKey!!)
                .setValue(message).addOnSuccessListener {
                    database!!.reference.child("chats")
                        .child(receiverRoom!!)
                        .child("message")
                        .child(randomKey)
                        .setValue(message)
                        .addOnSuccessListener { }
                }

        }
        binding.attachment.setOnClickListener {
            startActivityLauncher.launch("image/**")
        }

        val handler = Handler()
        binding.messageBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                database!!.reference.child("presence")
                    .child(senderUid!!)
                    .setValue("typing...")
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(userStoppedTying, 1000)

            }

            var userStoppedTying = Runnable {
                database!!.reference.child("presence")
                    .child(senderUid!!)
                    .setValue("Online")
            }
        })

    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currentId!!)
            .setValue("Online")
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currentId!!)
            .setValue("Offline")
    }

    private fun handleTask(result: Uri?) {
        if (result != null){
            val calendar = Calendar.getInstance()
            val reference = storage!!.reference.child("chats")
                .child(calendar.timeInMillis.toString() + "")
            dialog!!.show()
            reference.putFile(result)
                .addOnCompleteListener { task ->
                    dialog!!.dismiss()
                    if(task.isSuccessful){
                        reference.downloadUrl.addOnSuccessListener {uri ->
                            val filepath = uri.toString()
                            val messageTxt : String = binding.messageBox.text.toString()
                            val date = Date()
                            val message = Message(messageTxt, senderUid, date.time)
                            message.messsage = "photo"
                            message.imageUrl = filepath
                            binding.messageBox.setText("")
                            val randomKey = database!!.reference.push().key
                            val lastMsgObj = HashMap<String,Any>()
                            lastMsgObj["lastMsg"] = message.messsage!!
                            lastMsgObj["lastMsgTime"] = date.time
                            database!!.reference.child("chats")
                                .child(senderRoom!!)
                                .updateChildren(lastMsgObj)
                            database!!.reference.child("chats")
                                .child(receiverRoom!!)
                                .updateChildren(lastMsgObj)
                            database!!.reference.child("chats")
                                .child(senderRoom!!)
                                .child("message")
                                .child(randomKey!!)
                                .setValue(message).addOnSuccessListener {
                                    database!!.reference.child("chats")
                                        .child(receiverRoom!!)
                                        .child("message")
                                        .child(randomKey)
                                        .setValue(message)
                                        .addOnSuccessListener {}
                                }
                        }
                    }
                }
        }
    }

    private fun addToHisNotifications(hisUid: String, notification: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        val timestamp = ""+System.currentTimeMillis()
        val randomKey = database!!.reference.push().key
        val hashMap = HashMap<String,Any?>()
        hashMap["timestamp"] = timestamp
        hashMap["sUid"] = firebaseAuth.uid
        hashMap["notification"] = notification

        database!!.reference.child("userNotification")
            .child(hisUid)
            .child(randomKey!!)
            .setValue(hashMap)
            .addOnSuccessListener {}

    }

}