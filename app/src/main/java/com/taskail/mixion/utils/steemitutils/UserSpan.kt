package com.taskail.mixion.utils.steemitutils

import android.content.res.ColorStateList
import android.util.Log
import android.view.View
import com.taskail.mixion.utils.TouchableUrlSpan

/**
 *Created by ed on 1/30/18.
 *
 * Linkify a user
 *
 */
class UserSpan(private val userName: String,
               url: String,
               textColor: ColorStateList,
               pressedBackGroundColor: Int) :
        TouchableUrlSpan(url, textColor, pressedBackGroundColor) {

    override fun onClick(widget: View?) {

        //TODO - open user profile in new Activity

        Log.d("UserSpan", userName)

    }

}