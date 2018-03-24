package com.taskail.mixion.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.SteemitDataSource
import com.taskail.mixion.data.source.local.Drafts
import com.taskail.mixion.data.source.local.MixionDatabase
import com.taskail.mixion.main.steemitRepository
import com.taskail.mixion.utils.*
import kotlinx.android.synthetic.main.fragment_drafts.*

/**
 *Created by ed on 2/3/18.
 */

class DraftsFragment : BaseFragment(){

    private val TAG = javaClass.simpleName

    private lateinit var adapter: DraftsAdapter

    companion object {
        @JvmStatic fun newInstance(): DraftsFragment {
            return DraftsFragment()
        }
    }

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

        draftsRecycler.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)

        adapter  = DraftsAdapter(emptyList())

        draftsRecycler.adapter = adapter

        steemitRepository?.localRepository?.getDrafts(object :
                SteemitDataSource.DataLoadedCallback<Drafts>{
            override fun onDataLoaded(list: List<Drafts>) {
                adapter.drafts = list
            }

            override fun onDataLoaded(array: Array<Drafts>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onLoadError(error: Throwable) {
            }

        })

    }

    override fun onBackPressed(): Boolean {
        closeFragment(draftsContainer)
        return true
    }
}