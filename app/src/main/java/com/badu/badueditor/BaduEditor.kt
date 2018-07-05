package com.badu.badueditor

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import android.graphics.RectF
import android.graphics.drawable.Drawable
import kotlinx.android.synthetic.main.layout_content.view.*


class BaduEditor @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ImageLayout.OnDeleteButtonClickListener {

    private val margin = resources.getDimensionPixelSize(R.dimen.margin_normal)

    init {
        orientation = LinearLayout.VERTICAL
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupInitEditText()
    }

    override fun onDeleteButtonClicked(imageLayout: ImageLayout, index: Int) {
        linearLayout.removeView(imageLayout)
    }

    fun addImage(arg: Any, childIndex: Int) {
        val requestBuilder =
                Glide.with(this)
                        .asBitmap()

        when (arg) {
            is String -> requestBuilder.load(arg)
            is Uri -> requestBuilder.load(arg)
            else -> throw RuntimeException("Not Valid param for image downloading")
        }

        val imageLayout = ImageLayout(context).apply {
            id = childIndex
            onDeleteButtonClickListener = this@BaduEditor
        }

        val defaultWidth = width - 2 * margin
        val defaultHeight = defaultWidth * 9 / 16

        val params = LinearLayout.LayoutParams(defaultWidth, defaultHeight).apply {
            setMargins(margin, margin, margin, 0)
        }

        this@BaduEditor.post {
            addView(imageLayout, params)
        }

        requestBuilder
                .listener(object : RequestListener<Bitmap> {

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .into(object : ViewTarget<ImageLayout, Bitmap>(imageLayout) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                        val ratioWidth = width - 2 * margin
                        val ratioHeight = ratioWidth * resource.height / resource.width

                        this.view!!.layoutParams.run {
                            this.width = ratioWidth
                            this.height = ratioHeight
                        }

                        this.view.setImageBitmap(getRoundedBitmap(resource))
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)

                        val childView = this.view!!

                        childView.setBackgroundResource(android.R.color.black)
                    }
                })
    }

    private fun getRoundedBitmap(srcBitmap: Bitmap): Bitmap {
        synchronized(this) {
            val dstBitmap = Bitmap.createBitmap(
                    srcBitmap.width, // Width
                    srcBitmap.height, // Height

                    Bitmap.Config.ARGB_8888 // Config
            )

            val canvas = Canvas(dstBitmap)

            val paint = Paint()
            paint.isAntiAlias = true

            val rect = Rect(0, 0, srcBitmap.width, srcBitmap.height)

            val rectF = RectF(rect)

            val newRadius = srcBitmap.width * 0.05f
            canvas.drawRoundRect(rectF, newRadius, newRadius, paint)

            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

            canvas.drawBitmap(srcBitmap, 0f, 0f, paint)

            // cause Glide would hold the srcBitmap, for reuse reason, so we can not just recycler the srcBitmap
            /*  srcBitmap.recycle()*/

            return dstBitmap
        }
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