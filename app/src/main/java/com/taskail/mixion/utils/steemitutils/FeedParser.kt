package com.taskail.mixion.utils.steemitutils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

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