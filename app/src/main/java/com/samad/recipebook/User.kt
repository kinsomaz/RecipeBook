package com.samad.recipebook

class User {
    var uid: String? = null
    var name: String? = null
    var profileImage: String? = null

    constructor(){}
    constructor(
        uid: String?,
        name: String?,
        profileImage: String?
    ){
        this.uid = uid
        this.name = name
        this.profileImage = profileImage
    }



}