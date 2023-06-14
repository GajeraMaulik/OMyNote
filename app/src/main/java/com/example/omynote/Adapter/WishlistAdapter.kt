package com.example.omynote.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Models.Recipe.WishPerfumes
import com.example.omynote.Views.DashboardActivity
import com.example.omynote.Views.PerfumeDetailsActivity
import com.example.omynote.Views.expFlag
import com.example.omynote.databinding.ViewWishlistBinding
import com.squareup.picasso.Picasso
import java.net.URL

class WishlistAdapter: RecyclerView.Adapter<WishlistAdapter.ViewHolder>  {


    lateinit var mContext: Context
    var wishList: ArrayList<WishPerfumes> = ArrayList()

    constructor()
    constructor(context: Context,wishList:ArrayList<WishPerfumes>){
        this.mContext = context
        this.wishList = wishList
    }


    inner class ViewHolder(val binding: ViewWishlistBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(wishList[position]){

                binding.pdName.text = name
                binding.pdSubName.text = brand
                binding.progressBar.visibility = View.GONE
                binding.percentage.visibility = View.GONE
                binding.monthName.visibility = View.GONE
                Log.d("image","image $image")

                if (image == ""){

                }else{
                    val image = image?.replace("http","https")
                    Picasso.with(mContext).load(image).into(binding.pdImage)
                }

                binding.pdcardView.setOnClickListener {
                    expFlag = true
                    val i =  Intent(mContext, PerfumeDetailsActivity::class.java)
                    i.putExtra(AppConstant.perfumeId,pk)
                    mContext.startActivity(i)

                }


            }
        }
    }



    override fun getItemCount(): Int {
        return  wishList.size
    }
}