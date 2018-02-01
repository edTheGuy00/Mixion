package com.taskail.mixion.utils.steemitutils

import android.content.res.ColorStateList
import android.support.annotation.ColorInt
import android.text.Spanned
import android.util.Log
import com.taskail.mixion.utils.TouchableUrlSpan
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

    //TODO - fix this
    val urlSpans =
            spannableStringBuilder.getSpans(0, spannableStringBuilder.length, TouchableUrlSpan::class.java)

    for (urlSpan in urlSpans){

        val start = spannableStringBuilder.getSpanStart(urlSpan)

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

