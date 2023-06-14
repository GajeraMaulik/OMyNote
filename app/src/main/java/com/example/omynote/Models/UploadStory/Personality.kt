package com.example.omynote.Models.UploadStory

import com.google.gson.annotations.SerializedName

data class Personality(
    @SerializedName("scores"          ) var scores         : Scores?         = Scores(),
    @SerializedName("highest_emotion" ) var highestEmotion : HighestEmotion? = HighestEmotion()
)
