package auth

abstract class AuthProvider {
    abstract fun currentUser() : AuthUser
    abstract suspend fun login(email: String,password: String) : AuthUser
    abstract suspend fun createUser(email: String,password: String) : AuthUser
    abstract suspend fun logout()
    abstract suspend fun sendEmailVerification()
    abstract suspend fun sendPasswordReset(toEmail: String)

}