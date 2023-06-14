package com.example.omynote.Models.UploadStory

import com.google.gson.annotations.SerializedName

data class UploadStoryData(
    @SerializedName("resultat"    ) var resultat    : ArrayList<Resultat> = arrayListOf(),
    @SerializedName("personality" ) var personality : Personality?        = Personality(),
    @SerializedName("id"          ) var id          : Int?                = null
)
