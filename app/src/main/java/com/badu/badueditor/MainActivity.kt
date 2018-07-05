package com.badu.badueditor

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_content.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val RESULT_LOAD_IMG = 1
    }

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener {
            loadImageFromGallery()
        }

        button2.setOnClickListener {
            linearLayout.addImageFromUrl("http://img3.wikia.nocookie.net/__cb20131025223058/fantendo/images/2/25/Mario_Artwork_-_Super_Mario_3D_World.png")
            linearLayout.addImageFromUrl("https://www.cool3c.com/files/contentparty/abvjiRP.jpg")

        }


//        Glide.with(this)
//                .asBitmap()
//                .load("http://img3.wikia.nocookie.net/__cb20131025223058/fantendo/images/2/25/Mario_Artwork_-_Super_Mario_3D_World.png")
//                .into(object: SimpleTarget<Bitmap>(){
//                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                        Log.d("crazyma","width : ${resource.width}, ${resource.height}")
//                    }
//
//                })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            try {

                linearLayout.addImageFromUri(data!!.data)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun loadImageFromGallery(){
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
    }

}
