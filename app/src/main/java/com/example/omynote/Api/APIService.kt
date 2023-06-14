package com.example.omynote.Api

import com.example.omynote.Models.Login.LoginWithEmail
import com.example.omynote.Models.PerfumeDetails.PerfumeDetails
import com.example.omynote.Models.Recipe.RecipeModel
import com.example.omynote.Models.Register.RegisterData
import com.example.omynote.Models.Suggestion.SuggestionBody
import com.example.omynote.Models.Suggestion.SuggestionModel
import com.example.omynote.Models.UploadStory.UploadStoryData
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @FormUrlEncoded
    @Headers("Accept: application/json,text/plain")
    @POST("/rest-auth/login/")
    fun loginWithEmail(
     @Field("email") email :String,
     @Field("password") password:String
    ):Call<LoginWithEmail>

    @FormUrlEncoded
    @Headers("Accept: application/json,text/plain")
    @POST("/rest-auth/registration/")
    fun register(
        @Field("first_name") firstname:String,
        @Field("last_name") lastname:String,
        @Field("email") email :String,
        @Field("password") password:String,
        @Field("language") language :String
    ):Call<RegisterData>

    @GET("/clients/recipe/")
    fun getRecipe(
        @Header("Authorization") auth: String,
    ):Call<RecipeModel>

    @Headers("Accept: application/json")
    @DELETE("/clients/client/{clientId}/")
    fun accountDelete(
        @Header("Authorization") auth: String,
        @Path("clientId") clientId: Int
    ):Call<String>

   // @FormUrlEncoded
    @Headers("Accept: application/json")
    @GET("/v1/fdb0bfd9-7891-4864-bfa1-6a7826ef28bb")
    fun uploadStory(
       /* @Header("Authorization") auth: String,
        @Field("type") type :String,
        @Field("original_generated_text") original_generated_text:String,
        @Field("language") language:String,
        @Field("personality") personality:String*/
    ):Call<UploadStoryData>




   // @FormUrlEncoded
   // @Headers("Content-Type:application/json","Accept: application/json,text/plain")
    @GET("/v1/064231bd-b1b5-4468-8f1c-cb46c8837c43")
    fun suggestionList(
   /*    @Path("id") id:Int,
       @Body suggestion: SuggestionBody*/
   ):Call<SuggestionModel>

    @Headers("Accept: application/json,text/plain")
    @GET("/api/ada/perfume/{id}")
    fun getPerfumeDatail(
        @Header("Authorization") auth: String,
        @Path("id") id:Int
    ):Call<PerfumeDetails>


    /*
      @Field("selected_notes") selected_notes:ArrayList<Int>,
       @Field("added_notes") added_notes: ArrayList<String>,
       @Field("latitude") latitude:String,
       @Field("longitude") longitude:String,
       @Field("mode") mode: ArrayList<String>,
       @Field("saved_recipe") saved_recipe:Int,
       @Field("recipe_name") recipe_name:String*/
}