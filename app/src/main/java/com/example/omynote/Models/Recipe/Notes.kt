package com.example.omynote.Models.Recipe

import com.google.gson.annotations.SerializedName

data class Notes(
    @SerializedName("pk"          ) var pk          : Int?    = null,
    @SerializedName("name"        ) var name        : String? = null,
    @SerializedName("image"       ) var image       : String? = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("emotions"    ) var emotions    : String? = null

)
