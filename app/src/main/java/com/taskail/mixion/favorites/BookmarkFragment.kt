package com.taskail.mixion.favorites

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *Created by ed on 1/24/18.
 */
class BookmarkFragment : Fragment() {

    companion object {
        @JvmStatic fun newInstance(): BookmarkFragment {
            val fragment = BookmarkFragment()
            fragment.retainInstance = true
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}