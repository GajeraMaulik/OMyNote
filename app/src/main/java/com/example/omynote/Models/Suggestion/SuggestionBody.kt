package com.example.omynote.Models.Suggestion

import com.example.omynote.Models.Recipe.SelectedNotes
import com.google.gson.annotations.SerializedName

data class SuggestionBody(


     @SerializedName("id"             ) var id            : Int?              = null,
     @SerializedName("selected_notes" ) var selected_notes : List<Int?>?    = listOf(),
     @SerializedName("added_notes"    ) var added_notes    : List<String?>? = listOf(),
     @SerializedName("latitude"       ) var latitude      : String?           = null,
     @SerializedName("longitude"      ) var longitude     : String?           = null,
     @SerializedName("mode"           ) var mode          : List<String?>? = listOf(),
     @SerializedName("saved_recipe"   ) var saved_recipe   : Int?              = null,
     @SerializedName("recipe_name"    ) var recipe_name    : String?           = null,
)
 /*    constructor()
     constructor(id: Int,selected_notes: List<Int>,added_notes:List<String?>?,latitude:String?,longitude:String?,mode:List<String?>?,saved_recipe:Int,recipe_name:String?){
         this.id = id
         this.selected_notes = selected_notes
         this.added_notes = added_notes
         this.latitude = latitude
         this.longitude = longitude
         this.mode = mode
         this.saved_recipe = saved_recipe
         this.recipe_name = recipe_name
     }
 }*/


