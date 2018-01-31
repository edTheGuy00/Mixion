package com.taskail.mixion.utils

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.IOException
import java.util.regex.Pattern

/**
 *Created by ed on 1/24/18.
 */

fun String.getShorterSteemBody(){

}

/**
 *
 */
fun String.getFirstImgFromJsonMeta() : String? {
    var mainObject: JSONObject? = null
    try {
        mainObject = JSONObject(this)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    var imgObject: JSONArray? = null
    try {
        imgObject = if (mainObject != null) {
            mainObject.getJSONArray("image")
        } else {
            null
        }
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    var firstImg: String? = null
    try {
        firstImg = if (imgObject != null) {
            imgObject.get(0).toString()
        } else {
            null
        }
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    return firstImg
}

/**
 * an attempt to better parse the summary for the feedview
 */

fun String.parseFeedBody(): String?{

    val fromHtml = jsoupParser(this)
    try {
        val summary = extractShortSummary(0, 300, fromHtml)
        if (containsMarkdown(summary)){

        } else {
            return summary
        }
    } catch (e: IOException){
        e.printStackTrace()
    }


    return fromHtml

}

/**
 * this extracts a certain number of characters for a given string.
 */

@Throws(IOException::class, IndexOutOfBoundsException::class)
fun extractShortSummary(begin: Int, lenght: Int, s: String): String {
    return s.substring(begin, Math.min(s.length, lenght))
}

/**
 * Jsoup parser parses an html body and keeps line breaks
 */
fun jsoupParser(body: String): String{
    val newBody = Jsoup.parse(body.replace("(?i)<br[^>]*>", "br2n")).text()
    return newBody.replace("br2n", "\n")
}

private fun containsText(match: String, s: String): Boolean {
    return s.contains(match)
}

private fun containsMarkdown(s: String): Boolean {
    return s.indexOf('[') > -1
}

private fun extractMarkdown(body: String){
    val location = lastLocation(body)
}

private fun lastLocation(s: String): Int {
    return s.indexOf(')')
}

private fun removeUrl(input: String): String? {

    var withoutLink: String? = null

    val urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"
    val p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE)
    val m = p.matcher(input)
    var i = 0
    while (m.find()) {
        withoutLink = input.replace(Pattern.quote(m.group(i)).toRegex(), "").trim { it <= ' ' }
        i++
    }
    return withoutLink
}