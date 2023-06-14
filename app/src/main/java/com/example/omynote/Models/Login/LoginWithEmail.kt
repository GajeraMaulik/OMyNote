package com.example.omynote.Models.Login

import com.google.gson.annotations.SerializedName

 class LoginWithEmail {


   @SerializedName("key")
    var key : String? = null

    @SerializedName("api_key")
    var api_key : String? = null

     @SerializedName("type"             ) var type            : String = ""
     @SerializedName("detail"           ) var detail          : Detail? = Detail()
     @SerializedName("fallback_message" ) var fallbackMessage : String =""


}




