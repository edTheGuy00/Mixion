package com.taskail.mixion.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.SteemitDataSource
import com.taskail.mixion.data.source.local.Drafts
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

        adapter  = DraftsAdapter(mutableListOf(),
                {
                    draftItemSelected(it)
                },
                {
                    draftId, position -> deleteDraft(draftId, position)
                })

        draftsRecycler.adapter = adapter

        steemitRepository?.localRepository?.getDrafts(
                {
                    adapter.drafts = it.toMutableList()
                },
                {
                    Log.e(TAG, it.message)
                }
        )

    }

    private fun deleteDraft (id: String, pos: Int) {

        AlertDialog.Builder(this.context)
                .setTitle(R.string.delete_draft)
                .setPositiveButton(R.string.ok) { _, _ ->
                    steemitRepository?.localRepository?.deleteDraft(id)
                    adapter.drafts.removeAt(pos)
                    adapter.notifyDataSetChanged()
                }
                .setNegativeButton(R.string.cancel) {
                    onClick: DialogInterface, _: Int -> onClick.cancel()
                }
                .create()
                .show()
    }

    override fun onBackPressed(): Boolean {
        closeFragment(draftsContainer)
        return true
    }
}