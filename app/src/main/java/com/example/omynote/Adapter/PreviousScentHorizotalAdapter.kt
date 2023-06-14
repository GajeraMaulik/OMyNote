package com.example.omynote.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.omynote.Commons.CommonUtils
import com.example.omynote.Models.Recipe.BestMatchNotes
import com.example.omynote.Models.Recipe.FeaturedImage
import com.example.omynote.Models.Recipe.RecipeModel
import com.example.omynote.databinding.ViewPreviousScentsBinding
import com.example.omynote.databinding.ViewScentsBinding
import com.squareup.picasso.Picasso

class PreviousScentHorizotalAdapter: RecyclerView.Adapter<PreviousScentHorizotalAdapter.ViewHolder> {

    lateinit var context: Context
 var scentImageList: ArrayList<FeaturedImage> = ArrayList()
    constructor()
    constructor(context: Context,scentImageList: ArrayList<FeaturedImage>){
        this.context = context
        this.scentImageList = scentImageList
    }


    inner class ViewHolder(val binding: ViewScentsBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewScentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(scentImageList[position]){


                var image :String = image!!.replace("http","https")

                Picasso.with(context).load(image).into(binding.scentImage)

                Log.d("topThreeScentList", "\nscentImageList :  image : ${image} ")

            }
        }
    }


    override fun getItemCount(): Int {
        return scentImageList.size

    }


}