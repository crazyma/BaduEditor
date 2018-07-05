package com.badu.badueditor

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition

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

    fun addImageFromUri(imageUri: Uri) {
        Thread(Runnable {

            val imageStream = (context as Activity).contentResolver.openInputStream(imageUri)
            val selectedBitmap = BitmapFactory.decodeStream(imageStream)

            val imageView = ImageView(context).apply {
                setBackgroundResource(android.R.color.holo_red_light)
            }

            val ratioWidth = width - 2 * margin
            val ratioHeight = ratioWidth * selectedBitmap.height / selectedBitmap.width

            val scaledBitmap = Bitmap.createScaledBitmap(selectedBitmap, ratioWidth, ratioHeight, false)
            imageView.setImageBitmap(scaledBitmap)

            Log.d("crazyma", "image width: $ratioWidth, image height: $ratioHeight")

            val params = LinearLayout.LayoutParams(ratioWidth, ratioHeight).apply {
                setMargins(margin, margin, margin, 0)
            }

            this@BaduEditor.post {
                addView(imageView, params)
            }

        }).start()
    }

    fun addImageFromUrl(imageUrl: String) {

        val imageView = ImageView(context).apply {
            setBackgroundResource(android.R.color.holo_red_light)
        }

        val ratioWidth = width - 2 * margin
        val ratioHeight = ratioWidth * 9 / 16

        val params = LinearLayout.LayoutParams(ratioWidth, ratioHeight).apply {
            setMargins(margin, margin, margin, 0)
        }

        this@BaduEditor.post {
            addView(imageView, params)
        }

        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(object : ViewTarget<ImageView, Bitmap>(imageView) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                        Log.d("crazyma","!!width : ${resource.width}, height : ${resource.height}")

                        val ratioWidth = width - 2 * margin
                        val ratioHeight = ratioWidth * resource.height / resource.width

                        this.view!!.layoutParams.run {
                            this.width = ratioWidth
                            this.height = ratioHeight
                        }



                        this.view.setImageBitmap(resource)
                    }
                })

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

        addView(firstEditText, 0, params)
    }

}