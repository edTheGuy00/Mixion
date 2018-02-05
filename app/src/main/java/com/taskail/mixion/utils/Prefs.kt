package com.taskail.mixion.utils

/**
 *Created by ed on 2/4/18.
 */

fun saveUserName(user: String){
    setString("username", user)
}

fun getUserName(): String?{
    return getString("username", "none")
}
