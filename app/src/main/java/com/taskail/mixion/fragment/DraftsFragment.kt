package com.taskail.mixion.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.source.local.MixionDatabase
import com.taskail.mixion.utils.*
import kotlinx.android.synthetic.main.fragment_drafts.*

/**
 *Created by ed on 2/3/18.
 */

class DraftsFragment : BaseFragment(){

    private val TAG = javaClass.simpleName
    companion object {
        @JvmStatic fun newInstance(): DraftsFragment {
            return DraftsFragment()
        }
    }

    lateinit var database: MixionDatabase

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?):
            View? {
        return inflater.inflate(R.layout.fragment_drafts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        draftsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        draftsContainer.fadeInAnimation()

    }

    override fun onBackPressed(): Boolean {
        closeFragment(draftsContainer)
        return true
    }
}