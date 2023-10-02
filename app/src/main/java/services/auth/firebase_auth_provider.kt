package auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class FirebaseAuthProvider : AuthProvider() {
    override fun currentUser(): AuthUser {
        return AuthUser.fromFirebase
    }

    override suspend fun login(email: String, password: String): AuthUser {
        try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            val user = currentUser()
            return user
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                "user-not-found" -> {
                    throw UserNotFoundAuthException()
                }
                "wrong-password" -> {
                    throw WeakPasswordAuthException()
                }
                else -> {
                    throw GenericAuthException()
                }
            }
        } catch (e: Exception) {
            throw GenericAuthException()
        }
    }

    override suspend fun createUser(email: String, password: String): AuthUser {
        try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            return currentUser()
        } catch (e: FirebaseAuthException) {
            if (e.errorCode == "weak-password") {
                throw WeakPasswordAuthException()
            } else if (e.errorCode == "email-already-in-use") {
                throw EmailAlreadyInUseAuthException()
            } else if (e.errorCode == "invalid-email") {
                throw InvalidEmailAuthException()
            } else {
                throw GenericAuthException()
            }
        } catch (e: Exception) {
            throw GenericAuthException()
        }
    }

    override suspend fun logout() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            FirebaseAuth.getInstance().signOut()
        } else {
            throw UserNotLoggedInAuthException()
        }
    }

    override suspend fun sendEmailVerification(){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.sendEmailVerification()
        } else {
            throw UserNotLoggedInAuthException()
        }
    }

    override suspend fun sendPasswordReset(toEmail: String){
        try {
            FirebaseAuth.getInstance().sendPasswordResetEmail(toEmail)
        } catch (e: FirebaseAuthException) {
            if (e.errorCode == "firebase_auth/invalid-email") {
                throw InvalidEmailAuthException()
            } else if (e.errorCode == "firebase_auth/user-not-found") {
                throw UserNotFoundAuthException()
            } else {
                throw GenericAuthException()
            }
        } catch (e: Exception) {
            throw GenericAuthException()
        }
    }

}