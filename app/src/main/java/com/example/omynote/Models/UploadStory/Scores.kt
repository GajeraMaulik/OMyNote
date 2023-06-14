package com.example.omynote.Models.UploadStory

import com.google.gson.annotations.SerializedName

data class Scores(
    @SerializedName("fear"         ) var fear         : Double?= 0.0,
    @SerializedName("anger"        ) var anger        : Double? = 0.0,
    @SerializedName("anticipation" ) var anticipation : Double?    = 0.0,
    @SerializedName("trust"        ) var trust        : Double?    = 0.0,
    @SerializedName("surprise"     ) var surprise     : Double?    = 0.0,
    @SerializedName("positive"     ) var positive     : Double?    = 0.0,
    @SerializedName("negative"     ) var negative     : Double? = 0.0,
    @SerializedName("sadness"      ) var sadness      : Double? = 0.0,
    @SerializedName("disgust"      ) var disgust      : Double?    = 0.0,
    @SerializedName("joy"          ) var joy          : Double?    = 0.0
)