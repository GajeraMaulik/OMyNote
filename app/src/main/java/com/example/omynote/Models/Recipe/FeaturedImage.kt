package com.example.omynote.Models.Recipe

import com.google.gson.annotations.SerializedName

data class FeaturedImage(
    @SerializedName("id"     ) var id     : Int?    = null,
    @SerializedName("image"  ) var image  : String? = null,
    @SerializedName("origin" ) var origin : String? = null,
    @SerializedName("type"   ) var type   : String? = null
)
