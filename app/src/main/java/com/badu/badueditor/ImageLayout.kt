package com.badu.badueditor

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.layout_image_base.view.*

class ImageLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    interface OnDeleteButtonClickListener{
        fun onDeleteButtonClicked(imageLayout: ImageLayout)
    }

    var onDeleteButtonClickListener: OnDeleteButtonClickListener? = null

    init {
        inflate(context, R.layout.layout_image_base, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupDeleteButton()
    }

    private fun setupDeleteButton(){
        deleteImageView.setOnClickListener {
            onDeleteButtonClickListener?.onDeleteButtonClicked(this@ImageLayout)
        }
    }

    fun setImageBitmap(bitmap: Bitmap){
        mainImageView.setImageBitmap(bitmap)
    }
}