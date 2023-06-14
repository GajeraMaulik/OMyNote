package com.example.omynote.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omynote.Commons.ItemDecorator
import com.example.omynote.Models.Recipe.*
import com.example.omynote.databinding.ViewPreviousScentsBinding

class PreviousScentAdapter: RecyclerView.Adapter<PreviousScentAdapter.ViewHolder> {

    lateinit var context: Context
    lateinit var previousScentHorizotalAdapter : PreviousScentHorizotalAdapter
     var recipeList: ArrayList<Recipes> = ArrayList()
    var scentImageList: ArrayList<FeaturedImage> = ArrayList()
    var topThreeScentList: ArrayList<FeaturedImage> = ArrayList()
    var scentList:ArrayList<String> = ArrayList()
    var recipesList:ArrayList<String> = ArrayList()

    var previouList1=ArrayList<String>()
    var previouList2=ArrayList<String>()
    var previouList3=ArrayList<String>()

    constructor()
    constructor(context: Context,recipeList: ArrayList<Recipes>){
        this.context = context
        this.recipeList = recipeList
    }

init {
    previousScentHorizotalAdapter = PreviousScentHorizotalAdapter()
}
    inner class ViewHolder(val binding: ViewPreviousScentsBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewPreviousScentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(recipeList[position]){

                for (i in 0..recipeList.size-1){

                    binding.tvTitle.text = recipeName

                }

            var subString: String = ""
                for (i in 0..selectedNotes.size-1){
                    subString = subString + selectedNotes.get(i).name + ", "

                }
                subString = subString.dropLast(2);
                binding.tvSubTitle.setText(subString)

                Log.d("selectedate","selectedate  ${selectedNotes.size}")



               Log.e("scentList", "before scentList:  ${scentList.toString()} ")
                val scale: Float = context.resources.displayMetrics.density
                val spacevalue = -69 * scale + 0.5F

                var featuredImage = FeaturedImage()


                for (i in 0..recipeList.size-1){
                    topThreeScentList.clear()

                    val selectedNotes = recipeList.get(position).selectedNotes

                    for (i in 0..selectedNotes.size-1){

                        featuredImage =selectedNotes.get(i).featuredImage!!
                        topThreeScentList.add(featuredImage)
                        previousScentHorizotalAdapter = PreviousScentHorizotalAdapter(context,topThreeScentList)

                        binding.rvScents.adapter = previousScentHorizotalAdapter
                    }


                }

                Log.d("selectedate", "\ntopThreeScentList after add size : ${topThreeScentList.size}")
                Log.d("selectedate", "\ntopThreeScentList after add : ${topThreeScentList.toString()}")



                binding.rvScents.isHorizontalFadingEdgeEnabled = false

                binding.rvScents.addItemDecoration(ItemDecorator(spacevalue.toInt()))


                binding.scentView.setOnClickListener {

                }

            }
        }

    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}