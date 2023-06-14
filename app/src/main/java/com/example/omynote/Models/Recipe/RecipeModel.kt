package com.example.omynote.Models.Recipe

import com.google.gson.annotations.SerializedName

data class RecipeModel(

    @SerializedName("recipes" )
    var recipes : ArrayList<Recipes> =ArrayList(),
    @SerializedName("client"  )
    var client  : Client?            = Client()
)