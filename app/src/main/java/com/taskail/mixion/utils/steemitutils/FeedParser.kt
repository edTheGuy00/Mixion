package com.taskail.mixion.utils.steemitutils

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 *Created by ed on 1/31/18.
 */

/**
 * this extracts the first image that will be used in the feed
 * from the json metadata.
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
 * extract the summary that will be displayed on the feed
 */

//TODO- remove links
fun String.getFeedSummary(): String{

    // first convert from html
    val parsedHtml = fromHtml(this)

    //next extract markdown if there is any
    val parsedMd = stripMarkdownTags(parsedHtml.toString())

    //finally reduce to 300 characters, will throw an exception if body is empty
    var summary = ""
    try {
        summary = shortenString(0, 300, parsedMd)
    } catch (e: StringIndexOutOfBoundsException) {
        Log.e("FeedParse", "unable to shorten string " + e.message)
    }

    return summary
}

@Throws(IOException::class, IndexOutOfBoundsException::class)
fun shortenString(begin: Int, length: Int, s: String): String {
    return s.substring(begin, Math.min(s.length, length))
}

/**
 * An iterative approach to removing Markdown,
 */

private fun stripMarkdownTags(md: String): String {

    val sbText = StringBuilder()
    val sbMd = StringBuilder()

    var isText = true

    for (ch in md.toCharArray()) {
        if (isText) { // outside
            if (ch != '[') {
                sbText.append(ch)
                continue
            } else {   // switch mode
                isText = false
                sbMd.append(ch)
                continue
            }
        } else { // inside
            if (ch != ')') {
                sbMd.append(ch)
                continue
            } else {      // switch mode
                isText = true
                sbMd.append(ch)
                continue
            }
        }
    }

    return sbText.toString()
}