package com.taskail.mixion.utils.steemitutils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 *Created by ed on 3/12/18.
 */

/**
 * detect whether the post is from dtube,
 * 95% of the time they will begin as such..
 */

fun String.isFromDtube(): Boolean{
    return this.startsWith("<center><a href='https://d.")
}

private val gateways = arrayOf("https://ipfs.infura.io",
        "https://ipfs.io")

private fun canonicalGateway(ipfsHash: String): String {
    val g = ipfsHash.get(ipfsHash.length - 1).toInt() % gateways.size
    return gateways[g].split("://")[1]
}

fun getVideoUrl(hash: String): String {
    return "https://" + canonicalGateway(hash) + "/ipfs/" + hash
}

fun getVideoHash(json: String) : String? {
    var mainObject: JSONObject? = null
    try {
        mainObject = JSONObject(json)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    var videoObj: JSONObject? = null
    try {
        videoObj = if (mainObject != null) {
            mainObject.getJSONObject("video")
        } else {
            null
        }
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    var content: JSONObject? = null
    try {
        content = if (videoObj != null) {
            videoObj.getJSONObject("content")
        } else {
            null
        }
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    var hash: String? = null
    try {
        hash = if (content != null) {
            if (content.has("video480hash")){
                content.getString("video480hash")
            } else {
                content.getString("videohash")
            }
        } else {
            null
        }
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    return hash
}


