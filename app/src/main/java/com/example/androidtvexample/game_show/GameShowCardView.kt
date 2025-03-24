package com.example.androidtvexample.game_show

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.androidtvexample.R

class GameShowCardView @JvmOverloads constructor(
    private val context: Context,
    private val attrs: AttributeSet? = null

    ): ConstraintLayout(context, attrs) {
    init {
        isFocusable = true
        isFocusableInTouchMode = true
        isClickable = true
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.layout_item_gameshow, this)
    }

    override fun hasOverlappingRendering(): Boolean {
        return false
    }
}