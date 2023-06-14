package com.example.omynote.Models.PerfumeDetails

import com.example.omynote.Models.UploadStory.Resultat
import com.google.gson.annotations.SerializedName

data class PerfumeDetails(
    @SerializedName("pk"                   ) var pk                  : Int?              = null,
    @SerializedName("name"                 ) var name                : String?           = null,
    @SerializedName("image"                ) var image               : String?           = null,
    @SerializedName("brand"                ) var brand               : String?           = null,
    @SerializedName("notes"                ) var notes               : ArrayList<Notes>  = ArrayList(),
    @SerializedName("gender"               ) var gender              : String?           = null,
    @SerializedName("eco_friendly"         ) var ecoFriendly         : Boolean?          = null,
    @SerializedName("clean"                ) var clean               : Boolean?          = null,
    @SerializedName("exclusive"            ) var exclusive           : Boolean?          = null,
    @SerializedName("type"                 ) var type                : String?           = null,
    @SerializedName("affiliate_links"      ) var affiliateLinks      : ArrayList<String> = ArrayList(),
    @SerializedName("perfume"              ) var perfume             : Boolean?          = null,
    @SerializedName("hair"                 ) var hair                : Boolean?          = null,
    @SerializedName("body"                 ) var body                : Boolean?          = null,
    @SerializedName("home"                 ) var home                : Boolean?          = null,
    @SerializedName("oil"                  ) var oil                 : Boolean?          = null,
    @SerializedName("background_image"     ) var backgroundImage     : String?           = null,
    @SerializedName("olfactive_family"     ) var olfactiveFamily     : String?           = null,
    @SerializedName("year_of_release"      ) var yearOfRelease       : String?           = null,
    @SerializedName("sustainability"       ) var sustainability      : String?           = null,
    @SerializedName("vegan"                ) var vegan               : Boolean?          = null,
    @SerializedName("natural"              ) var natural             : String?           = null,
    @SerializedName("neutral"              ) var neutral             : String?           = null,
    @SerializedName("lasting"              ) var lasting             : String?           = null,
    @SerializedName("spillage"             ) var spillage            : String?           = null,
    @SerializedName("description"          ) var description         : String?           = null,
    @SerializedName("associated_occasions" ) var associatedOccasions : String?           = null,
    @SerializedName("product_url"          ) var productUrl          : String?           = null,
    @SerializedName("video"                ) var video               : String?           = null,
    @SerializedName("layer"                ) var layer               : ArrayList<Layer>  = ArrayList(),
    @SerializedName("emotions"             ) var emotions            : ArrayList<String> = ArrayList()

)
