package com.taskail.mixion.ui.dialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.taskail.mixion.R
import com.taskail.mixion.data.SteemitRepository
import kotlinx.android.synthetic.main.dialog_tags.*

/**
 *Created by ed on 1/26/18.
 */

class TagDialog(context: Context?,
                private val repository: SteemitRepository,
                private val func: (String) -> Unit) :
        AppCompatDialog(context) {

    val TAG = javaClass.simpleName

    init {
        setContentView(R.layout.dialog_tags)
        window.setBackgroundDrawableResource(android.R.color.transparent)
    }

    interface TagDialogCallback{
        fun onTagSelected(tag: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        touchable_background.setOnClickListener {
            this.dismiss()
        }

        recyclerView.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)

        val adapter = TagDialogAdapter(AdapterCallback(), ArrayList(0))

        recyclerView.adapter = adapter

        repository.getTags({
            adapter.tags = it
        }, {
            Log.e(TAG, it.message)
        })
    }

    inner class AdapterCallback : TagDialogCallback{

        override fun onTagSelected(tag: String) {
            this@TagDialog.dismiss()
            func(tag)
        }
    }
}