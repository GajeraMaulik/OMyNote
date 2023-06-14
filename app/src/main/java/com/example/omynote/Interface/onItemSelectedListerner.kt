package com.example.omynote.Interface

import java.text.ParsePosition

public interface onItemSelectedListerner {

    fun onItemSelected(filterName:String,filterId: Int)
    fun onItemunSelected(arraylist:String,intArrayList: Int)


}