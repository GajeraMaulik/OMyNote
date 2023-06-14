package com.example.omynote.Models.PerfumeDetails

import com.google.gson.annotations.SerializedName

data class Layer(
    @SerializedName("pk"    ) var pk    : Int?    = null,
    @SerializedName("name"  ) var name  : String? = null,
    @SerializedName("image" ) var image : String? = null,
    @SerializedName("brand" ) var brand : String? = null

)
