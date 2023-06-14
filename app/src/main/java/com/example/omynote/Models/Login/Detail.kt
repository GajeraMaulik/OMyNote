package com.example.omynote.Models.Login

import com.google.gson.annotations.SerializedName

data class Detail(
    @SerializedName("non_field_errors" ) var nonFieldErrors : ArrayList<String> = arrayListOf()
)
