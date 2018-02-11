package com.taskail.mixion.post

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.markortexteditor.highlighter.HighlightingEditor
import com.taskail.mixion.markortexteditor.TextFormat
import kotlinx.android.synthetic.main.fragment_new_post_edit_body.*

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

    lateinit var textFormat: TextFormat
    lateinit var textModuleActionBar: ViewGroup
    lateinit var hlEditor: HighlightingEditor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_new_post_edit_body, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        textModuleActionBar = edit_post_action_bar
        textFormat = TextFormat.getFormat(TextFormat.FORMAT_MARKDOWN, activity)
        hlEditor = document__fragment__edit__highlighting_editor

        hlEditor.setHighlighter(textFormat.highlighter)
        textFormat.textModuleActions
                .setHighlightingEditor(hlEditor)
                .appendTextModuleActionsToBar(textModuleActionBar)
    }
}