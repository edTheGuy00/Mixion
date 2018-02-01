package com.taskail.mixion.utils.steemitutils

import java.util.regex.Pattern

/**
 * detect whether the post is from dtube,
 * 95% of the time they will begin as such..
 */

fun String.isFromDtube(): Boolean{
    return this.startsWith("<center><a href='https://d.")
}

/**
 * detects if there is a youtube link in the post with a valid
 * youtube id
 */

fun String.containsYoutubeVideo(): Boolean{
    //TODO - this regex actually detects any type of video, including dtube videos
    val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"

    val compiledPattern = Pattern.compile(pattern)

    val matcher = compiledPattern.matcher(this)

    return matcher.find()
}

/**
 * extracts the Youtube Id
 */
fun String.getYoutubeId(): String{
    //TODO - this regex actually detects any type of video, including dtube videos
    val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"

    val compiledPattern = Pattern.compile(pattern)

    val matcher = compiledPattern.matcher(this)

    return matcher.group()
}