package com.samad.recipebook

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.samad.recipebook.databinding.DeleteLayoutBinding
import com.samad.recipebook.databinding.SendMsgBinding

class MessageAdapter(var context: Context, messages: ArrayList<Message>?, senderRoom: String, receiveRoom: String)
    : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    lateinit var messages: ArrayList<Message>
    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2
    val senderRoom: String
    var receiveRoom: String


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType==ITEM_SENT){
            val view = LayoutInflater.from(context).inflate(R.layout.send_msg,parent,false)
            SentMsgHolder(view)
        }
        else{
            val view = LayoutInflater.from(context).inflate(R.layout.receive_msg,parent,false)
            ReceiveMsgHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val messages = messages[position]
        return if (FirebaseAuth.getInstance().uid == messages.senderId){
            ITEM_SENT
        }
        else{
            ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = messages[position]
        if (holder.javaClass == SentMsgHolder::class.java){
            val viewHolder = holder as SentMsgHolder
            if (message.messsage.equals("photo")){
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.mlinear.visibility = View.GONE
                Glide.with(context)
                    .load(message.imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .into(viewHolder.binding.image)
            }
            viewHolder.binding.message.text = message.messsage
            viewHolder.itemView.setOnLongClickListener {

                val view = LayoutInflater.from(context)
                    .inflate(R.layout.delete_layout,null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)

                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()
                binding.everyone.setOnClickListener {
                    message.messsage = "This message is removed"
                    message.messsageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1).setValue(message)
                    }
                    message.messsageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(receiveRoom)
                            .child("message")
                            .child(it1).setValue(message)
                    }
                    dialog.dismiss()
                }
                binding.delete.setOnClickListener {
                    message.messsageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1).setValue(null)
                    }
                    dialog.dismiss()

                }
                binding.cancel.setOnClickListener { dialog.dismiss() }
                dialog.show()
                false
            }
        }
        else{

            val viewHolder = holder as ReceiveMsgHolder
            if(message.messsage.equals("photo")){
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.mlinear.visibility = View.GONE
                Glide.with(context)
                    .load(message.imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .into(viewHolder.binding.image)
            }

            viewHolder.binding.message.text = message.messsage
            viewHolder.itemView.setOnLongClickListener {

                val view = LayoutInflater.from(context)
                    .inflate(R.layout.delete_layout, null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)

                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()
                binding.everyone.setOnClickListener {
                    message.messsage = "This message is removed"
                    message.messsageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1).setValue(message)
                    }
                    message.messsageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(receiveRoom)
                            .child("message")
                            .child(it1).setValue(message)
                    }
                    dialog.dismiss()
                }
                binding.delete.setOnClickListener {
                    message.messsageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1).setValue(null)
                    }
                    dialog.dismiss()

                }
                binding.cancel.setOnClickListener { dialog.dismiss() }
                dialog.show()

                false

            }


        }

    }


    inner class SentMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: SendMsgBinding = SendMsgBinding.bind(itemView)
    }

    inner class ReceiveMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: SendMsgBinding = SendMsgBinding.bind(itemView)
    }

    init {
        if(messages != null){
            this.messages = messages
        }
        this.senderRoom = senderRoom
        this.receiveRoom = receiveRoom
    }
}