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
import android.view.View
import kotlinx.android.synthetic.main.layout_content.view.*


class BaduEditor @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
        View.OnFocusChangeListener,
        ImageLayout.OnDeleteButtonClickListener {

    companion object {
        const val INSERT_STATE_START = 0x01
        const val INSERT_STATE_MIDDLE = 0x02
        const val INSERT_STATE_END = 0x03
    }

    private var insertState = INSERT_STATE_START
    private val margin = resources.getDimensionPixelSize(R.dimen.margin_normal)
    private var operatingEditText: EditText? = null
    private var operatingCorsorIndex = 0

    init {
        orientation = LinearLayout.VERTICAL
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        insertEditText()
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (v != null && v is EditText && hasFocus) {
            operatingEditText = v
        }
    }

    override fun onDeleteButtonClicked(imageLayout: ImageLayout) {
        linearLayout.removeView(imageLayout)
    }

    fun getOperatingCursorIndex() {
        operatingEditText?.run {
            operatingCorsorIndex = selectionStart
            insertState = when (selectionStart) {
                length() -> {
                    INSERT_STATE_END
                }

                0 -> {
                    INSERT_STATE_START
                }

                else -> {
                    INSERT_STATE_MIDDLE
                }
            }
        }
    }

    fun addImage(arg: Any, childIndex: Int) {
        if (arg !is String && arg !is Uri) {
            throw RuntimeException("Not Valid param for image downloading")
        }

        when (insertState) {

            INSERT_STATE_START -> {
                val imageLayout = insertImageLayout(0)
                loadImage(arg, imageLayout)
                postDelayed({ insertEditText(0) }, 200)

            }

            INSERT_STATE_END -> {
                val childCount = childCount
                val imageLayout = insertImageLayout(childCount)
                loadImage(arg, imageLayout)
                postDelayed({ insertEditText(childCount + 1) }, 200)

            }

            INSERT_STATE_MIDDLE -> {
            }
        }
    }

    private fun insertEditText(childIndex: Int = 0) {
        val firstEditText = EditText(context).apply {
            setBackgroundResource(android.R.color.holo_green_light)
            onFocusChangeListener = this@BaduEditor
        }

        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(margin, margin, margin, 0)
        }

        addView(firstEditText, childIndex, params)
    }

    fun insertImageLayout(childIndex: Int): ImageLayout {
        val imageLayout = ImageLayout(context).apply {
            onDeleteButtonClickListener = this@BaduEditor
        }

        val defaultWidth = width - 2 * margin
        val defaultHeight = defaultWidth * 9 / 16

        val params = LinearLayout.LayoutParams(defaultWidth, defaultHeight).apply {
            setMargins(margin, margin, margin, 0)
        }

        this@BaduEditor.post {
            addView(imageLayout, childIndex, params)
        }

        return imageLayout
    }

    fun loadImage(arg: Any, imageLayout: ImageLayout) {
        val requestBuilder =
                Glide.with(this)
                        .asBitmap()

        when (arg) {
            is String -> requestBuilder.load(arg)
            is Uri -> requestBuilder.load(arg)
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

    fun addImageOld(arg: Any, childIndex: Int) {
        val requestBuilder =
                Glide.with(this)
                        .asBitmap()

        when (arg) {
            is String -> requestBuilder.load(arg)
            is Uri -> requestBuilder.load(arg)
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

}