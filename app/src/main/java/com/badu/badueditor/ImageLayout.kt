package com.badu.badueditor

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.layout_image_base.view.*

class ImageLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.layout_image_base, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupDeleteButton()
    }

    private fun setupDeleteButton(){

    }

    fun setImageBitmap(bitmap: Bitmap){
        mainImageView.setImageBitmap(bitmap)
    }
}