package com.example.omynote.Models.Register

import com.google.gson.annotations.SerializedName

data class RegisterData(
    @SerializedName("key")
    var key    : String? = null,

    @SerializedName("api_key" )
    var apiKey : String? = null
)
