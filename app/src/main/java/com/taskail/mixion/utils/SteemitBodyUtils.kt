package com.taskail.mixion.utils

import android.content.res.ColorStateList
import android.support.annotation.ColorInt
import android.text.Spanned
import android.util.Log
import okhttp3.HttpUrl

/**
 *Created by ed on 1/30/18.
 *
 * An attempt to linkify a steemit user account on a discussion
 *
 */

fun parseBodyHtml(input: String,
                  linkTextColor: ColorStateList,
                  @ColorInt linkHighlightColor: Int) : Spanned {

    val spannableStringBuilder = parseHtml(input, linkTextColor, linkHighlightColor)

    Log.d("parseBody", spannableStringBuilder.getSpanStart(0).toString())

    val urlSpans =
            spannableStringBuilder.getSpans(0, spannableStringBuilder.length, TouchableUrlSpan::class.java)

    for (urlSpan in urlSpans){

        val start = spannableStringBuilder.getSpanStart(urlSpan)

        Log.d("start", spannableStringBuilder.subSequence(start, 1).toString())

        if(spannableStringBuilder.subSequence(start, start.plus(1)).toString() == "@"){
            val end = spannableStringBuilder.getSpanEnd(urlSpan)

            spannableStringBuilder.removeSpan(urlSpan)

            val url = HttpUrl.parse(urlSpan.url)

            val userName = url?.pathSegments()?.get(0)

            spannableStringBuilder.setSpan(
                    UserSpan(userName!!,
                            urlSpan.url,
                            linkTextColor,
                            linkHighlightColor),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

    }

    return spannableStringBuilder
}

fun String.isFromDtube(): Boolean{
    return this.startsWith("<center><a href='https://d.tube")
}