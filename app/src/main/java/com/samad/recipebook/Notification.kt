package com.samad.recipebook

class Notification {
    var timestamp: String? = null
    var sUid: String? = null
    var notification: String? = null

    constructor() {}
    constructor(
        timestamp: String,
        sUid: String,
        notification: String,
    ){
        this.timestamp = timestamp
        this.sUid = sUid
        this.notification = notification
    }

}