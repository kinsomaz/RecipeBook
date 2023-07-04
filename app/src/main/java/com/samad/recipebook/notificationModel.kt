package com.samad.recipebook

data class NotificationModel(var title: String, var date: String) {

    object Supplier{
        val notificationModels = listOf<NotificationModel>(
            NotificationModel("Adebayo Apercu sent you a message", "Yesterday"),
            NotificationModel("Oladele Tamilore replied your message", "Monday 22 October"),
            NotificationModel("20 people added your recipe as a favourite", "Yesterday")
        )
    }
}