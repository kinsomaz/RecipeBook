package com.samad.recipebook

class RecipeData{

    var title: String? = null
    var bitmap: Any? = null

    constructor(){}
    constructor(
        title: String?,
        bitmap: Any?
    ){
        this.title = title
        this.bitmap = bitmap
    }
}
