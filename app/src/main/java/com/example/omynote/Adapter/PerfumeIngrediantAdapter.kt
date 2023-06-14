package com.example.omynote.Adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.omynote.Models.PerfumeDetails.Notes
import com.example.omynote.Models.UploadStory.Resultat
import com.example.omynote.R
import com.example.omynote.databinding.ActivityIngrediantsBinding
import com.example.omynote.databinding.ViewIngrediantBinding
import com.squareup.picasso.Picasso

class PerfumeIngrediantAdapter: RecyclerView.Adapter<PerfumeIngrediantAdapter.ViewHolder> {

    lateinit var context: Context
    var notesList:ArrayList<Notes> = ArrayList()
    constructor()
    constructor(context: Context,notesList :ArrayList<Notes>){
        this.context = context
        this.notesList = notesList
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding: ViewIngrediantBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewIngrediantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(notesList[position]){

                binding.ingrediantName.text = name
                //  binding.ingrediantView.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                if (image == ""){

                }else{
                    val image = image?.replace("http","https")
                    com.squareup.picasso.Picasso.with(context).load(image).into(binding.ingrediantImage)
                }
            }


        }
    }

    override fun getItemCount(): Int {
      return  notesList.size
    }
}