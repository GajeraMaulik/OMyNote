package com.example.omynote.Adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.example.omynote.Commons.AppConstant
import com.example.omynote.Commons.ApplicationInitialize
import com.example.omynote.Interface.onItemSelectedListerner
import com.example.omynote.Models.Suggestion.MatchedPerfumes
import com.example.omynote.Models.Suggestion.SuggestionModel
import com.example.omynote.Views.PerfumeDetailsActivity
import com.example.omynote.Views.expFlag
import com.example.omynote.databinding.ViewWishlistBinding
import com.squareup.picasso.Picasso

var tvgender:String = ""
var filterValue:Boolean = false
class DiscoverAdapter: RecyclerView.Adapter<DiscoverAdapter.ViewHolder>   {


    lateinit var mContext: Context
    var discoverList: ArrayList<SuggestionModel> = ArrayList()
    var matchedPerfumesList :MutableList<MatchedPerfumes> = ArrayList()
    var originalPerfumesList :MutableList<MatchedPerfumes> = ArrayList()
    var name:String = ""
    var brand:String = ""
    var image:String = ""
    var fetch_accuracy:Int = 0

    constructor()
    constructor(context: Context, discoverList:ArrayList<SuggestionModel>, matchedPerfumesList :MutableList<MatchedPerfumes>){
        this.mContext = context
        this.discoverList = discoverList
        this.matchedPerfumesList = matchedPerfumesList
        this.originalPerfumesList = matchedPerfumesList
    }


    inner class ViewHolder(val binding: ViewWishlistBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(matchedPerfumesList[position]){
                   // var data = discoverList[position]


                 //   tvgender = data.mode
             //   for (i in 0..matchedPerfumes.size-1){

                    if (name == ""){

                    }else{
                        binding.pdName.text= name

                    }

                    if (isFav == 0){
                        binding.itemFavorite.isChecked = false
                    }else{
                        binding.itemFavorite.isChecked = true
                    }




                    if (image == ""){

                    }else{
                        val image = image?.replace("http","https")
                        Picasso.with(mContext).load(image).into(binding.pdImage)
                    }
                    binding.monthName.visibility = View.VISIBLE

                    if(brand == ""){

                    }else{
                        binding.pdSubName.text  = brand

                    }
                    if (fetchAccuracy == 0){

                    }else{
                        binding.percentage.text =" ${fetchAccuracy.toString()}%"

                        binding.progressBar.progress = fetchAccuracy!!

                    }
                if (gender == ""){

                }else{
                    tvgender = gender.toString()
                }
                binding.pdcardView.setOnClickListener {
                    expFlag = true
                    val i =  Intent(mContext, PerfumeDetailsActivity::class.java)
                    i.putExtra(AppConstant.perfumeId,pk)
                    i.putExtra(AppConstant.fetchAccuracy,fetchAccuracy)
                    mContext.startActivity(i)

                }

                Log.d("image","image $image")




            }
        }
    }

    override fun getItemCount(): Int {
        return  matchedPerfumesList.size
    }

    fun updateList(newList: ArrayList<MatchedPerfumes>) {
        matchedPerfumesList = newList
        notifyDataSetChanged()
    }

    fun addList(newList: ArrayList<MatchedPerfumes>){
        matchedPerfumesList.addAll(newList)
    }


}


