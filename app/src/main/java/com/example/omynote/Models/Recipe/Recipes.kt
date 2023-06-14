package com.example.omynote.Models.Recipe

import com.google.gson.annotations.SerializedName

data class Recipes(
    @SerializedName("id"                ) var id               : Int?                     = null,
    @SerializedName("mode"              ) var mode             : ArrayList<String>        = arrayListOf(),
    @SerializedName("recipe_name"       ) var recipeName       : String?                  = null,
    @SerializedName("creation_datetime" ) var creationDatetime : String?                  = null,
    @SerializedName("selected_notes"    ) var selectedNotes    : ArrayList<SelectedNotes> = ArrayList(),
    @SerializedName("added_notes"       ) var addedNotes       : ArrayList<String>        = arrayListOf()
)
