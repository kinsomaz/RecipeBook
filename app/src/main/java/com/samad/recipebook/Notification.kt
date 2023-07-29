package com.samad.recipebook

class Notification {
    var timestamp: String? = null
    var pUid: String? = null
    var notification: String? = null

    constructor() {}
    constructor(
        timestamp: String,
        pUid: String,
        notification: String,
    ){
        this.timestamp = timestamp
        this.pUid = pUid
        this.notification = notification
    }

}