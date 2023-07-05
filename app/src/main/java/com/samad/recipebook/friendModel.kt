package com.samad.recipebook

data class Friend(var name: String, var follower: String, var image: Int){

    object Supplier{
        val friends = listOf<Friend>(
            Friend("Adebayo Apercu","100K Friends",R.drawable.chat_image1),
            Friend("Ronke Igboegwu","200 Friends",R.drawable.chat_image2),
            Friend("Ekaite Akande","10K Friends",R.drawable.chat_image3),
            Friend("Adedunni Nwaneri","190 Friends",R.drawable.chat_image4)
        )
    }
}