package com.example.omynote.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.omynote.Commons.SharePref
import com.example.omynote.Models.LanguageData
import com.example.omynote.R
import com.example.omynote.Views.lcKey
import com.example.omynote.databinding.ViewLanguageBinding
import com.example.omynote.databinding.ViewPreviousScentsBinding

class LanguageAdapter:RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    lateinit var context:Context
    var languageList:ArrayList<LanguageData> = ArrayList()

    var selectedItemPos = 0
    var lastItemSelectedPos = 0
    constructor()
    constructor(context: Context,languageList: ArrayList<LanguageData>){
        this.context = context
        this.languageList = languageList
    }
    inner class ViewHolder(val binding: ViewLanguageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun defaultBg() {
            binding.cdLangview.background = context.getDrawable(R.drawable.bg_chip)
        }

        fun selectedBg() {
            binding.cdLangview.background = context.getDrawable(R.drawable.bg_select_chip)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(languageList[position]){



                if (position == selectedItemPos){
                    holder.selectedBg()
                }else{
                    holder.defaultBg()
                }

                binding.tvLanguage.text = language
                binding.cdLangview.setOnClickListener {
                    lcKey = key!!
                    SharePref.toast(context, lcKey)
                    selectedItemPos = adapterPosition
                    if(lastItemSelectedPos == -1)
                        lastItemSelectedPos = selectedItemPos
                    else {
                        notifyItemChanged(lastItemSelectedPos)
                        lastItemSelectedPos = selectedItemPos
                    }
                    notifyItemChanged(selectedItemPos)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return languageList.size
    }


}