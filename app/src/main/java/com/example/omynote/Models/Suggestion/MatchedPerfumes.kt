package com.example.omynote.Models.Suggestion

import com.google.gson.annotations.SerializedName

data class MatchedPerfumes(
    @SerializedName("pk"              ) var pk             : Int?              = null,
    @SerializedName("name"            ) var name           : String?           = null,
    @SerializedName("image"           ) var image          : String?           = null,
    @SerializedName("brand"           ) var brand          : String?           = null,
    @SerializedName("notes"           ) var notes          : ArrayList<Notes>  = arrayListOf(),
    @SerializedName("gender"          ) var gender         : String?           = null,
    @SerializedName("eco_friendly"    ) var ecoFriendly    : Boolean?          = null,
    @SerializedName("exclusive"       ) var exclusive      : Boolean?          = null,
    @SerializedName("affiliate_links" ) var affiliateLinks : ArrayList<String> = arrayListOf(),
    @SerializedName("clean"           ) var clean          : Boolean?          = null,
    @SerializedName("collection"      ) var collection     : String?           = null,
    @SerializedName("perfume"         ) var perfume        : Boolean?          = null,
    @SerializedName("hair"            ) var hair           : Boolean?          = null,
    @SerializedName("body"            ) var body           : Boolean?          = null,
    @SerializedName("home"            ) var home           : Boolean?          = null,
    @SerializedName("oil"             ) var oil            : Boolean?          = null,
    @SerializedName("fetch_accuracy"  ) var fetchAccuracy  : Int?              = null,
    var isFav :Int = 0
)
