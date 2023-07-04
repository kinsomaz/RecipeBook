package com.samad.recipebook

data class Chat (var name: String, var latestMessage: String){

    object Supplier{
        val chats = listOf<Chat>(
            Chat("Adebayo Apercu","A selection of chicken dishes served together ..."),
            Chat("Ronke Igboegwu","A selection of chicken dishes served together ..."),
            Chat("Ekaite Akande","A selection of chicken dishes served together ..."),
            Chat("Adedunni Nwaneri","A selection of chicken dishes served together ...")
        )
    }
}
