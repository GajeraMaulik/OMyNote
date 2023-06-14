package com.example.omynote.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.omynote.Commons.ApplicationInitialize
import com.example.omynote.Interface.onItemSelectedListerner
import com.example.omynote.Models.FilterData
import com.example.omynote.Models.Suggestion.MatchedPerfumes
import com.example.omynote.R
import com.example.omynote.databinding.ViewFilterBinding


var filterName:String =""

class FilterAdapter: RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    lateinit var context: Context
    var filterList:ArrayList<FilterData> = ArrayList()
    var selectedItemPos = 0
    var lastItemSelectedPos = 0
    lateinit var onItemSelectedListerner: onItemSelectedListerner
    constructor()
    constructor(context: Context, filterList: ArrayList<FilterData>,onItemSelectedListerner: onItemSelectedListerner){
        this.context = context
        this.filterList = filterList
        this.onItemSelectedListerner = onItemSelectedListerner
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ViewFilterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun defaultBg() {
            binding.cdFilterView.background = context.getDrawable(R.drawable.bg_filter)
            binding.filterName.setTextColor(Color.parseColor("#2D2D2D"))
            binding.filterIcon.setColorFilter(Color.parseColor("#9C9C9C"))
        }

        fun selectedBg() {
            binding.cdFilterView.background = context.getDrawable(R.drawable.bg_select_chip)
            binding.filterName.setTextColor(Color.parseColor("#ffffff"))
            binding.filterIcon.setColorFilter(Color.parseColor("#ffffff"))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(filterList[position]){




                if (position == selectedItemPos  ){
                    holder.selectedBg()
                }else{
                    holder.defaultBg()
                }
                binding.filterIcon.setImageResource(image)
                binding.filterName.text = title

              //  val matchedPerfumes = MatchedPerfumes()

                binding.cdFilterView.setOnClickListener {

                        filterName = title
                       // updateFilter(title,matchedPerfumes)

                        onItemSelectedListerner.onItemSelected(title,id)
                    selectedItemPos = adapterPosition



                       // SharePref.toast(context,"$selectedItemPos")
                    if(lastItemSelectedPos == -1) {
                        lastItemSelectedPos = selectedItemPos
                    }else{
                        notifyItemChanged(lastItemSelectedPos)
                        lastItemSelectedPos = selectedItemPos
                    }
                    notifyItemChanged(selectedItemPos)
                }
                }

            }
        }


    override fun getItemCount(): Int {
        return  filterList.size
    }

    fun updateFilter(filterText:String,matchedPerfumes: MatchedPerfumes) {
        if(ApplicationInitialize.filterSuggestionMap.size != 0 ){
            if (matchedPerfumes.perfume.toString() == filterText){
                if (matchedPerfumes.perfume == true) {
                    ApplicationInitialize.filterSuggestionMap[matchedPerfumes.pk!!] =
                        matchedPerfumes

                    notifyDataSetChanged()
                }
                //  discoverAdapter.updateList(ApplicationInitialize.filterSuggestionMap[arrayListOf(matchedPerfumes.pk!!)])

            }


        }
    }
}