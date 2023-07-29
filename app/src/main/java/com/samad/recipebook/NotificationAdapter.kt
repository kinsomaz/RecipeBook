package com.samad.recipebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samad.recipebook.databinding.NotificationViewBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class NotificationAdapter (val context: Context , private val notification:List<Notification>): RecyclerView.Adapter<NotificationAdapter.MyViewHolder>(){

    private lateinit var binding: NotificationViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notification_view, parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notification.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val notification = notification[position]

        val timeStamp = notification.timestamp
        val calendar = Calendar.getInstance(Locale.getDefault())
        if (timeStamp != null) {
            calendar.timeInMillis = timeStamp.toLong()
        }
        val pTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString()

        holder.title.text = notification.notification
        holder.date.text = pTime
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val title = itemView.findViewById<TextView>(R.id.notificationTitle)
        val date = itemView.findViewById<TextView>(R.id.notificationDate)

    }
}

