package com.example.omynote.Models.Recipe

import com.google.gson.annotations.SerializedName

data class Client(

    @SerializedName("id"                     ) var id                   : Int?                    = null,
    @SerializedName("wish_perfumes"          ) var wishPerfumes         : ArrayList<WishPerfumes> = arrayListOf(),
    @SerializedName("followings"             ) var followings           : ArrayList<String>       = arrayListOf(),
    @SerializedName("followers"              ) var followers            : ArrayList<String>       = arrayListOf(),
    @SerializedName("my_perfumes"            ) var myPerfumes           : ArrayList<String>       = arrayListOf(),
    @SerializedName("is_deleted"             ) var isDeleted            : Boolean?                = null,
    @SerializedName("email"                  ) var email                : String?                 = null,
    @SerializedName("image"                  ) var image                : String?                 = null,
    @SerializedName("first_name"             ) var firstName            : String?                 = null,
    @SerializedName("last_name"              ) var lastName             : String?                 = null,
    @SerializedName("age"                    ) var age                  : String?                 = null,
    @SerializedName("skin_type"              ) var skinType             : String?                 = null,
    @SerializedName("gender"                 ) var gender               : String?                 = null,
    @SerializedName("allergies"              ) var allergies            : String?                 = null,
    @SerializedName("public"                 ) var public               : Boolean?                = null,
    @SerializedName("send_samples"           ) var sendSamples          : Boolean?                = null,
    @SerializedName("newsletter"             ) var newsletter           : Boolean?                = null,
    @SerializedName("address"                ) var address              : String?                 = null,
    @SerializedName("city"                   ) var city                 : String?                 = null,
    @SerializedName("zip_code"               ) var zipCode              : String?                 = null,
    @SerializedName("country"                ) var country              : String?                 = null,
    @SerializedName("language"               ) var language             : String?                 = null,
    @SerializedName("default_story_language" ) var defaultStoryLanguage : String?                 = null,
    @SerializedName("interested_in"          ) var interestedIn         : String?                 = null,
    @SerializedName("status"                 ) var status               : String?                 = null,
    @SerializedName("latitude"               ) var latitude             : String?                 = null,
    @SerializedName("longitude"              ) var longitude            : String?                 = null,
    @SerializedName("creation_datetime"      ) var creationDatetime     : String?                 = null,
    @SerializedName("user"                   ) var user                 : Int?                    = null

)
