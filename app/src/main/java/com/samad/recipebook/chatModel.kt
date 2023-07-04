package com.samad.recipebook

data class Chat(var name: String, var latestMessage: String, var image: Int){

    object Supplier{
        val chats = listOf<Chat>(
            Chat("Adebayo Apercu","A selection of chicken dishes served together ...",R.drawable.chat_image1),
            Chat("Ronke Igboegwu","A selection of chicken dishes served together ...",R.drawable.chat_image2),
            Chat("Ekaite Akande","A selection of chicken dishes served together ...",R.drawable.chat_image3),
            Chat("Adedunni Nwaneri","A selection of chicken dishes served together ...",R.drawable.chat_image4)
        )
    }
}
