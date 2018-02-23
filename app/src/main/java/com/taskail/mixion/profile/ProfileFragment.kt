package com.taskail.mixion.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.BaseFragment
import com.taskail.mixion.R
import com.taskail.mixion.utils.*
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 *Created by ed on 2/3/18.
 */

class ProfileFragment : BaseFragment(){

    private val TAG = javaClass.simpleName
    companion object {
        @JvmStatic fun newInstance(): ProfileFragment{
            return ProfileFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        profileContainer.fadeInAnimation()
    }

    override fun onBackPressed(): Boolean {
        closeFragment(profileContainer)
        return true
    }
}