package com.taskail.mixion.utils

import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.graphics.Canvas
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import android.graphics.Rect
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

/**
 *Created by ed on 1/31/18.
 *
 * image getter to load images into html bodies
 *
 */

class GlideImageGetter(private val target: TextView) :
        Html.ImageGetter{

    var targets = ArrayList<Target<Bitmap>>()

    override fun getDrawable(p0: String?): Drawable {
        val load: RequestBuilder<Bitmap> = Glide.with(target.context).asBitmap().load(p0)
        val urlDrawable = UrlDrawable()
        val target = BitmapTarget(urlDrawable)
        targets.add(target)
        load.into(target)

        return urlDrawable
    }

    inner class BitmapTarget(private val urlDrawable: UrlDrawable) : SimpleTarget<Bitmap>(){


        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

            val drawable = BitmapDrawable(target.resources, resource)

            target.post( {

                val w = target.width
                val hh = drawable.intrinsicHeight
                val ww = drawable.intrinsicWidth
                val newHeight = hh * w / ww
                val rect = Rect(0, 0, w, newHeight)

                //TODO - make image match parent
                /**if (resource.width > target.width) {
                    val aspectRatio = resource.height.toFloat().div( resource.width.toFloat())
                    bitmapDrawable.setBounds(0, 0, target.width, (aspectRatio * target.width).toInt())
                    rect = Rect(0, 0, target.width, (aspectRatio * target.width).toInt())
                } else {
                    bitmapDrawable.setBounds(0, 0, resource.width, resource.height)
                    rect = Rect(0, 0, resource.width, resource.height)
                } */
                drawable.bounds = rect
                urlDrawable.bounds = rect
                urlDrawable.drawable = drawable
                target.text = target.text
                target.invalidate()
            })

        }

    }


    @Suppress("DEPRECATION")
    inner class UrlDrawable : BitmapDrawable() {

        var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            drawable?.draw(canvas)
        }
    }


}