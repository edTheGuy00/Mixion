package com.taskail.mixion.post

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R

/**
 *Created by ed on 2/9/18.
 */

class EditPostFragment : Fragment(){

    companion object {
        @JvmStatic fun newInstance(): EditPostFragment{
            return EditPostFragment()
        }

        val FRAGMENT_TAG = "EditPostFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_new_post_edit_body, container, false)

        return view
    }
}