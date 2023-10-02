package auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthUser {
    private var id : String? = null
    private var email : String? = null
    private var isEmailVerified : Boolean? = null

    constructor(
        user: FirebaseUser
    ){
        this.id = user.uid
        this.email = user.email
        this.isEmailVerified = user.isEmailVerified
    }
    constructor(
        id : String?,
        email : String?,
        isEmailVerified : Boolean?
    ){
        this.id = id
        this.email = email
        this.isEmailVerified = isEmailVerified
    }

    companion object {
        val fromFirebase = AuthUser(FirebaseAuth.getInstance().currentUser!!)
    }



}