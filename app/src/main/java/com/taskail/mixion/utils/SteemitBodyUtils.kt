package com.taskail.mixion.utils

import android.content.res.ColorStateList
import android.support.annotation.ColorInt
import android.text.Spanned
import android.util.Log
import okhttp3.HttpUrl
import java.util.regex.Pattern

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

/**
 * detect whether the post is from dtube,
 * 95% of the time they will begin as such..
 */
fun String.isFromDtube(): Boolean{
    return this.startsWith("<center><a href='https://d.tube")
}

/**
 * detects if there is a youtube link in the post with a valid
 * youtube id
 */

fun String.containsYoutubeVieo(): Boolean{

    val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"

    val compiledPattern = Pattern.compile(pattern)

    val matcher = compiledPattern.matcher(this)

    return matcher.find()
}

/**
 * extracts the Youtube Id
 */
fun String.getYoutubeId(): String{

    val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"

    val compiledPattern = Pattern.compile(pattern)

    val matcher = compiledPattern.matcher(this)

    return matcher.group()
}