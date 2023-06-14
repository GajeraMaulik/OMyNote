package com.example.omynote.Models.Recipe

import com.google.gson.annotations.SerializedName

data class SelectedNotes(
    @SerializedName("pk"                ) var pk               : Int?                      = null,
    @SerializedName("best_match_notes"  ) var bestMatchNotes   : ArrayList<BestMatchNotes> = arrayListOf(),
    @SerializedName("featured_image"    ) var featuredImage    : FeaturedImage?            = FeaturedImage(),
    @SerializedName("accord"            ) var accord           : String?                   = null,
    @SerializedName("countries"         ) var countries        : ArrayList<String>         = arrayListOf(),
    @SerializedName("note_id"           ) var noteId           : String?                   = null,
    @SerializedName("name"              ) var name             : String?                   = null,
    @SerializedName("name_fr"           ) var nameFr           : String?                   = null,
    @SerializedName("name_arabic"       ) var nameArabic       : String?                   = null,
    @SerializedName("emotions"          ) var emotions         : String?                   = null,
    @SerializedName("emotions_fr"       ) var emotionsFr       : String?                   = null,
    @SerializedName("type"              ) var type             : String?                   = null,
    @SerializedName("description"       ) var description      : String?                   = null,
    @SerializedName("parts"             ) var parts            : String?                   = null,
    @SerializedName("extraction_method" ) var extractionMethod : String?                   = null,
    @SerializedName("history"           ) var history          : String?                   = null,
    @SerializedName("effect"            ) var effect           : String?                   = null,
    @SerializedName("iconique_name"     ) var iconiqueName     : String?                   = null,
    @SerializedName("iconique_brand"    ) var iconiqueBrand    : String?                   = null,
    @SerializedName("average_score"     ) var averageScore     : Int?                      = null,
    @SerializedName("active"            ) var active           : Boolean?                  = null,
    @SerializedName("creation_datetime" ) var creationDatetime : String?                   = null

)
