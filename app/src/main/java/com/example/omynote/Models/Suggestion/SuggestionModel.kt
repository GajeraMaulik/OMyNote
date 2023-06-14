package com.example.omynote.Models.Suggestion

import com.google.gson.annotations.SerializedName

data class SuggestionModel(
    @SerializedName("id"               ) var id              : Int?                       = null,
    @SerializedName("client"           ) var client          : Int?                       = null,
    @SerializedName("latitude"         ) var latitude        : String?                    = null,
    @SerializedName("longitude"        ) var longitude       : String?                    = null,
    @SerializedName("mode"             ) var mode            : ArrayList<String?>?          = arrayListOf(),
    @SerializedName("saved_recipe"     ) var saved_recipe     : Boolean?                   = null,
    @SerializedName("recipe_name"      ) var recipeName      : String?                    = null,
    @SerializedName("selected_notes"   ) var selectedNotes   : ArrayList<Int?>?             = arrayListOf(),
    @SerializedName("added_notes"      ) var addedNotes      : ArrayList<String?>?          = arrayListOf(),
    @SerializedName("matched_perfumes" ) var matchedPerfumes : ArrayList<MatchedPerfumes> = ArrayList()

)
