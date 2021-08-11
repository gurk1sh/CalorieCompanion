package se.umu.cs.guth0028.caloriecompanionapp.foodResources

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point

/*
* Class for scaling and displaying bitmaps
* Loads images into a bitmap object that stores pixel data so it can be shown to user
* */


    fun getScaledBitmap(path: String, activity: Activity): Bitmap { //Conservative scale function for checking screen size and then scaling the image down to that size
        val size = Point()
        activity.windowManager.defaultDisplay.getSize(size)
        return getScaledBitmap(path, size.x, size.y)
    }

    fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap { //Scales the bitmap down by hand and then rereads it to make a Bitmap object
        // Fetch the image on disk and its dimensions
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        val srcWidth = options.outWidth.toFloat()
        val srcHeight = options.outHeight.toFloat()
        // Calculate how much it should be scaled
        var inSampleSize = 1
        if (srcHeight > destHeight || srcWidth > destWidth) {
            val heightScale = srcHeight / destHeight
            val widthScale = srcWidth / destWidth
            val sampleScale = if (heightScale > widthScale) {
                heightScale
            } else {
                widthScale
            }
            inSampleSize = Math.round(sampleScale)
        }
        options = BitmapFactory.Options()
        options.inSampleSize = inSampleSize
        // Create final bitmap
        return BitmapFactory.decodeFile(path, options)
    }