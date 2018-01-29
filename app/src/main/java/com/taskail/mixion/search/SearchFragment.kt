package com.taskail.mixion.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.BackPressedHandler
import com.taskail.mixion.R
import com.taskail.mixion.utils.getCallback
import com.taskail.mixion.utils.hideSoftKeyboard

/**
 *Created by ed on 1/29/18.
 */
class SearchFragment : Fragment(), BackPressedHandler {

    interface Callback{
        fun onSearchClosed()
        fun onSearchResultSelected()
    }

    companion object {
        @JvmStatic fun newInstance(): SearchFragment{
            return SearchFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onBackPressed() : Boolean {
        closeSearchFragment()

        return true
    }

    private fun closeSearchFragment(){

        //TODO - add a cool exit animation
        view?.hideSoftKeyboard()
        val callback = callback()
        callback?.onSearchClosed()

    }

    private fun callback(): Callback? {
        return getCallback(this, Callback::class.java)
    }

}