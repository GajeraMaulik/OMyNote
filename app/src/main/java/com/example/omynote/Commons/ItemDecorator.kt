package com.example.omynote.Commons

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecorator:RecyclerView.ItemDecoration {

    private var mSpace = 0

    constructor()
    constructor(space: Int) {
        mSpace = space
    }

   override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        /*if (position != 0)*/ outRect.right = mSpace
    }
}