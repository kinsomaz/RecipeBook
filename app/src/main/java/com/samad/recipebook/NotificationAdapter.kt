package com.samad.recipebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.samad.recipebook.databinding.NotificationViewBinding

private lateinit var binding: NotificationViewBinding

class NotificationAdapter (val context: Context , private val notificationModels:List<NotificationModel>): RecyclerView.Adapter<NotificationAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = NotificationViewBinding.inflate(LayoutInflater.from(context), parent ,false)
        val view = binding.root
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notificationModels.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val notificationModel = notificationModels[position]
        holder.setData(notificationModel, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun setData(notificationModel: NotificationModel?, pos: Int){
            notificationModel?.let {
                binding.notificationTitle.text = notificationModel.title
                binding.notificationDate.text = notificationModel.date
            }
        }

    }
}

