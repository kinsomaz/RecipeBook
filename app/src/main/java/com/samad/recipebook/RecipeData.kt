package com.samad.recipebook

import android.icu.text.CaseMap.Title

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
