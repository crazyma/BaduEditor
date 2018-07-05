package com.badu.badueditor

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout

class BaduEditor @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val margin = resources.getDimensionPixelSize(R.dimen.margin_normal)

    init {
        orientation = LinearLayout.VERTICAL
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupInitEditText()
    }

    private fun setupInitEditText() {
        val firstEditText = EditText(context).apply {
            setBackgroundResource(android.R.color.holo_green_light)
        }

        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(margin, margin, margin, 0)
        }

        addView(firstEditText,0,params)
    }

}