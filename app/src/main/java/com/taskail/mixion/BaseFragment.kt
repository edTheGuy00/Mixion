package com.taskail.mixion

import android.support.v4.app.Fragment
import android.view.View
import com.taskail.mixion.utils.FadeOutCallBack
import com.taskail.mixion.utils.fadeOutAnimation
import com.taskail.mixion.utils.getCallback

/**
 *Created by ed on 2/23/18.
 */
abstract class BaseFragment: Fragment(), BackPressedHandler {

    interface Callback {
        fun onFragmentClosed()
        fun onSearchResultSelected(author: String, permlink: String)
    }

    protected fun closeFragment(view: View) {
        view.fadeOutAnimation(object : FadeOutCallBack {
            override fun onAnimationEnd() {
                val callback = callback()
                callback?.onFragmentClosed()
            }
        })
    }

    protected fun searchItemSelected(author: String, permlink: String){
        callback()?.onSearchResultSelected(author, permlink)
    }

    private fun callback(): Callback? {
        return getCallback(this, Callback::class.java)
    }
}