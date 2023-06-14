package com.example.omynote.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.omynote.Commons.SharePref
import com.example.omynote.Interface.onItemSelectedListerner
import com.example.omynote.Models.PerfumeDetails.Notes
import com.example.omynote.Models.UploadStory.Resultat
import com.example.omynote.R
import com.example.omynote.databinding.ActivityIngrediantsBinding
import com.example.omynote.databinding.ActivityPerfumeDetailsBinding
import com.example.omynote.databinding.ViewIngrediantBinding
import com.squareup.picasso.Picasso

var resultatIdList:ArrayList<Int> = ArrayList()

class IngrediantAdapter: RecyclerView.Adapter<IngrediantAdapter.ViewHolder> {

    lateinit var context: Context
    var resultatList:ArrayList<Resultat> = ArrayList()
    lateinit var activityIngrediantsBinding : ActivityIngrediantsBinding
    var nextFlag :Boolean = false
    constructor()
    constructor(context: Context, resultatList: ArrayList<Resultat>, activityIngrediantsBinding : ActivityIngrediantsBinding,nextFlag:Boolean){
        this.context = context
        this.resultatList = resultatList
        this.activityIngrediantsBinding = activityIngrediantsBinding
        this.nextFlag = nextFlag
        notifyDataSetChanged()
    }
    constructor(context: Context, resultatList:ArrayList<Resultat>){
        this.context = context
        this.resultatList = resultatList
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding: ViewIngrediantBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewIngrediantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(resultatList[position]){

                binding.ingrediantName.text = name
              //  binding.ingrediantView.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                if (image == ""){

                }else{
                    val image = image?.replace("http","https")
                    Picasso.with(context).load(image).into(binding.ingrediantImage)
                }


                binding.ingrediantView.setOnClickListener {
                    if (isSelected){
                        isSelected = false

                        resultatIdList.add(pk!!)

                        if (resultatIdList.size >= 3){
                            nextFlag = true
                            activityIngrediantsBinding.btnNextIngrediant.isEnabled = true
                            activityIngrediantsBinding.btnNextIngrediant.setBackgroundResource(R.drawable.bg_btn)
                            activityIngrediantsBinding.btnNextIngrediant.setTextColor(Color.parseColor("#FFFFFFFF"))

                        }
                        binding.ingrediantView.setBackgroundColor(Color.parseColor("#A9BCD0"))
                        binding.ingrediantName.setTextColor(Color.parseColor("#FFFFFF"))


                    }else{
                        isSelected = true
                        resultatIdList.remove(pk)

                        if (resultatIdList.size <= 3){
                            nextFlag  = false
                            activityIngrediantsBinding.btnNextIngrediant.isEnabled = false

                            activityIngrediantsBinding.btnNextIngrediant.setBackgroundResource(R.drawable.bg_btn_disable)
                            activityIngrediantsBinding.btnNextIngrediant.setTextColor(Color.parseColor("#B8B8B8"))

                        }

                        binding.ingrediantView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        binding.ingrediantName.setTextColor(Color.parseColor("#2D2D2D"))
                    }
                    Log.d("resultatIdList","size : ${resultatIdList.size}\n resultatIdList : ${resultatIdList.toString()}")

                }
            }


        }
    }

    override fun getItemCount(): Int {
        return  resultatList.size
    }
}