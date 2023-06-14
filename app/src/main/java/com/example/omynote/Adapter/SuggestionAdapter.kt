package com.example.omynote.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.omynote.Models.Suggestion.SuggestionModel
import com.example.omynote.databinding.ViewWishlistBinding

class SuggestionAdapter: RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {

    lateinit var mContext: Context
    var suggestionList: ArrayList<SuggestionModel> = ArrayList()

    constructor()
    constructor(context: Context, suggestionList:ArrayList<SuggestionModel>){
        this.mContext = context
        this.suggestionList = suggestionList
    }


    inner class ViewHolder(val binding: ViewWishlistBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(suggestionList[position]){


            }
        }
    }

    override fun getItemCount(): Int {
        return  suggestionList.size
    }
}