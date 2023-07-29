package com.samad.recipebook

class Message {

    var messsageId : String? = null
    var messsage : String? = null
    var senderId : String? = null
    var imageUrl : String? = null
    var timeStamp : Long = 0

    constructor(){}
    constructor(
        message :String?,
        senderId :String?,
        timeStamp :Long
    ){
        this.messsage = message
        this.senderId = senderId
        this.timeStamp = timeStamp
    }
}