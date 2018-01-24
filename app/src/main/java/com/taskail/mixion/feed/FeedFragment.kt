package com.taskail.mixion.feed

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *Created by ed on 1/24/18.
 */
class FeedFragment : Fragment(), FeedContract.View {


    override lateinit var  presenter: FeedContract.Presenter

    override fun showFeed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        @JvmStatic fun newInstance(): FeedFragment {
            val fragment = FeedFragment()
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