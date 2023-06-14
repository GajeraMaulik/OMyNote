package com.example.omynote.Commons

import android.content.Context
import android.os.DropBoxManager
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.omynote.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarkerView(context: Context, layoutResource: Int): MarkerView(context, layoutResource){
    private val tvContent: TextView = findViewById(R.id.tv_content)

    // This method will be called whenever a marker is drawn
    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        // Set the text content of the marker
        tvContent.setText(entry!!.y.toInt())
        Log.e("CustomMarkerView"," value y : ${entry.y}\n\n x : ${entry.x}\n\n data : ${entry.data}")
        super.refreshContent(entry, highlight)
    }

    // Set the position of the marker
    override fun getOffset(): MPPointF {
        return MPPointF(((width / 2)).toFloat(), (height).toFloat())
    }
}