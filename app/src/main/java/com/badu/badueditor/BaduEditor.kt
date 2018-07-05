package com.badu.badueditor

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.util.AttributeSet
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import timber.log.Timber
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.RectF





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

    @Deprecated("useless")
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

            Timber.d("image width: $ratioWidth, image height: $ratioHeight")

            val params = LinearLayout.LayoutParams(ratioWidth, ratioHeight).apply {
                setMargins(margin, margin, margin, 0)
            }

            this@BaduEditor.post {
                addView(imageView, params)
            }

        }).start()
    }

    fun addImageFromGalleryUri(imageUri: Uri) {

        val imageView = ImageView(context).apply {
            //            setBackgroundResource(android.R.color.holo_red_light)
        }

        val ratioWidth = width - 2 * margin
        val ratioHeight = ratioWidth * 9 / 16

        val params = LinearLayout.LayoutParams(ratioWidth, ratioHeight).apply {
            setMargins(margin, margin, margin, 0)
        }

        this@BaduEditor.post {
            addView(imageView, params)
        }

        val multi = MultiTransformation(
                RoundedCornersTransformation(margin * 5, 0, RoundedCornersTransformation.CornerType.ALL))

        Glide.with(this)
                .asBitmap()

                .load(imageUri)
                .listener(object : RequestListener<Bitmap> {

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        Timber.e("listener onLoadFailed   " + e?.toString())
                        return true
                    }
                })
                .into(object : ViewTarget<ImageView, Bitmap>(imageView) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                        val ratioWidth = width - 2 * margin
                        val ratioHeight = ratioWidth * resource.height / resource.width

                        Timber.d("ratio width: $ratioWidth, height: $ratioHeight")

                        this.view!!.layoutParams.run {
                            this.width = ratioWidth
                            this.height = ratioHeight
                        }

                        this.view.setImageBitmap(getRoundedBitmap(resource))
                    }
                })

    }

    fun addImageFromUrl(imageUrl: String) {

        val imageView = ImageView(context).apply {
            //            setBackgroundResource(android.R.color.holo_red_light)
        }

        val ratioWidth = width - 2 * margin
        val ratioHeight = ratioWidth * 9 / 16

        val params = LinearLayout.LayoutParams(ratioWidth, ratioHeight).apply {
            setMargins(margin, margin, margin, 0)
        }

        this@BaduEditor.post {
            addView(imageView, params)
        }

        val multi = MultiTransformation(
                RoundedCornersTransformation(60, 0, RoundedCornersTransformation.CornerType.ALL))

        Glide.with(this)
                .asBitmap()
//                .apply(bitmapTransform(multi))
                .load(imageUrl)
                .listener(object : RequestListener<Bitmap> {

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        Timber.e("listener onLoadFailed   " + e?.toString())
                        return true
                    }
                })
                .into(object : ViewTarget<ImageView, Bitmap>(imageView) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {



                        val ratioWidth = width - 2 * margin
                        val ratioHeight = ratioWidth * resource.height / resource.width

                        Timber.d("ratio width: $ratioWidth, height: $ratioHeight")

                        this.view!!.layoutParams.run {
                            this.width = ratioWidth
                            this.height = ratioHeight
                        }

                        this.view.setImageBitmap(getRoundedBitmap(resource))
                    }
                })

    }

    private fun getRoundedBitmap(srcBitmap: Bitmap, cornerRadius: Float = 40f):Bitmap {
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

        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        canvas.drawBitmap(srcBitmap, 0f, 0f, paint)

        srcBitmap.recycle()

        return dstBitmap
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