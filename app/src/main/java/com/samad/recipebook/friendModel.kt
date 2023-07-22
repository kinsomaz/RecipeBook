package com.samad.recipebook

data class Friend(var name: String, var follower: String, var image: Int){

    object Supplier{
        val friends = listOf<Friend>(
            Friend("Adebayo Apercu","100K Friends",R.drawable.image_avatar),
            Friend("Ronke Igboegwu","200 Friends",R.drawable.image_avatar),
            Friend("Ekaite Akande","10K Friends",R.drawable.image_avatar),
            Friend("Adedunni Nwaneri","190 Friends",R.drawable.image_avatar)
        )
    }
}