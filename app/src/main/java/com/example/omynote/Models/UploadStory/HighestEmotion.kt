package com.example.omynote.Models.UploadStory

import com.google.gson.annotations.SerializedName

data class HighestEmotion(
    @SerializedName("id"                         ) var id                       : Int?    = null,
    @SerializedName("emotion_name"               ) var emotionName              : String? = null,
    @SerializedName("personality_name"           ) var personalityName          : String? = null,
    @SerializedName("personality_name_fr"        ) var personalityNameFr        : String? = null,
    @SerializedName("personality_description"    ) var personalityDescription   : String? = null,
    @SerializedName("personality_description_fr" ) var personalityDescriptionFr : String? = null

)
