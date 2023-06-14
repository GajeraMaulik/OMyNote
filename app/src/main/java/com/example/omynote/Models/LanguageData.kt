package com.example.omynote.Models

import com.google.gson.annotations.SerializedName

data class LanguageData(
    @SerializedName("key")
    var key : String? = null,

    @SerializedName("language")
    var language : String? = null
)
