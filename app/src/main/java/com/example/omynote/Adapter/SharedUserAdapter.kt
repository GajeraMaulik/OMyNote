package com.example.omynote.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.omynote.Models.Recipe.WishPerfumes
import com.example.omynote.Models.ShareUserData
import com.example.omynote.Models.Suggestion.MatchedPerfumes
import com.example.omynote.Models.Suggestion.SuggestionModel
import com.example.omynote.databinding.ViewEmotionSharedBinding
import com.example.omynote.databinding.ViewWishlistBinding
import com.squareup.picasso.Picasso

class SharedUserAdapter: RecyclerView.Adapter<SharedUserAdapter.ViewHolder> {


    lateinit var mContext: Context
    var shareUserList: ArrayList<ShareUserData> = ArrayList()

    constructor()
    constructor(context: Context, shareUserList:ArrayList<ShareUserData>){
        this.mContext = context
        this.shareUserList = shareUserList
    }
    inner class ViewHolder(val binding: ViewEmotionSharedBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewEmotionSharedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      with(holder){
          with(shareUserList[position]){


              if (image == null){

              }else{
                //  val image = image?.replace("http","https")
                  binding.profileImage.setImageResource(image!!)
               //   Picasso.with(mContext).load(image).into(binding.pdImage)
              }
                binding.tvUsernamePd.text =  name
                binding.tvEmotionSharedPd.text = emotion
          }
      }
    }

    override fun getItemCount(): Int {
       return shareUserList.size
    }

}