package com.taskail.mixion.dialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialog
import android.support.v7.widget.LinearLayoutManager
import com.taskail.mixion.R
import com.taskail.mixion.data.SteemitRepository
import kotlinx.android.synthetic.main.dialog_tags.*

/**
 *Created by ed on 1/26/18.
 */
class TagDialog(context: Context?,
                private val repository: SteemitRepository,
                private val callback: TagDialogCallback) :
        AppCompatDialog(context) {

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
    }
}