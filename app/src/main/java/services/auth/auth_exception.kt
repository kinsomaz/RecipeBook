package auth

// login exception
class UserNotFoundAuthException : Exception() {}

class WrongPasswordAuthException : Exception() {}

// register exception
class WeakPasswordAuthException : Exception() {}

class EmailAlreadyInUseAuthException : Exception() {}

class InvalidEmailAuthException : Exception() {}

// generic exceptions
class GenericAuthException : Exception() {}

class UserNotLoggedInAuthException : Exception() {}
