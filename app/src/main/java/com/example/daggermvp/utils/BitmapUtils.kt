package com.example.daggermvp.utils

import android.graphics.Bitmap

object BitmapUtils {

    fun scaleBitmapDown(bitmap: Bitmap?, maxDimension: Int): Bitmap? {
        val originalWidth = bitmap?.width
        val originalHeight = bitmap?.height
        var resizedWidth = maxDimension
        var resizedHeight = maxDimension
        if (originalHeight != null) {
            when {
                originalHeight > originalWidth!! -> {
                    resizedHeight = maxDimension
                    resizedWidth = (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
                }
                originalWidth > originalHeight -> {
                    resizedWidth = maxDimension
                    resizedHeight = (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
                }
                originalHeight == originalWidth -> {
                    resizedHeight = maxDimension
                    resizedWidth = maxDimension
                }
            }
        }
        return Bitmap.createScaledBitmap(bitmap!!, resizedWidth, resizedHeight, false)
    }

}