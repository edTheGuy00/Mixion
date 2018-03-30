package com.taskail.mixion.steemdiscussion

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.ui.animation.DismissableAnimation
import com.taskail.mixion.ui.animation.RevealAnimationSettings
import com.taskail.mixion.ui.animation.registerCircularRevealAnimation
import com.taskail.mixion.ui.animation.startCircularExitAnimation
import com.taskail.mixion.ui.markortexteditor.TextFormat
import com.taskail.mixion.ui.markortexteditor.highlighter.HighlightingEditor
import com.taskail.mixion.utils.hideSoftKeyboard
import kotlinx.android.synthetic.main.fragment_create_reply.*

/**
 *Created by ed on 3/29/18.
 */

class CreateReplyFragment :
        Fragment(),
        DiscussionContract.ReplyView,
        DismissableAnimation {

    override lateinit var presenter: DiscussionContract.Presenter

    lateinit var title: String

    companion object {

        const val ARG_REVEAL = "args_reveal"

        fun newInstance(revealAnimationSettings: RevealAnimationSettings) : CreateReplyFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARG_REVEAL, revealAnimationSettings)
            val fragment = CreateReplyFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var textFormat: TextFormat
    private lateinit var textModuleActionBar: ViewGroup
    private lateinit var hlEditor: HighlightingEditor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_reply, container, false)

        val revealAnim: RevealAnimationSettings = arguments?.getParcelable(ARG_REVEAL)!!
        registerCircularRevealAnimation(view!!,
                revealAnim,
                ContextCompat.getColor(context!!, R.color.primary),
                ContextCompat.getColor(context!!, R.color.white))

        return  view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textModuleActionBar = edit_post_action_bar
        textFormat = TextFormat.getFormat(TextFormat.FORMAT_MARKDOWN, activity)
        hlEditor = document__fragment__edit__highlighting_editor
        setupHlEditor()

        backBtn.setOnClickListener {
            presenter.dismissReplyFragment()
        }

        replyTitle.text = "RE: $title"

        publishButton.setOnClickListener {
            if (!hlEditor.text.isNullOrEmpty()) {
                publishComment(getContent())
            }
        }
    }

    private fun publishComment(content: String) {
        presenter.postDiscussionreply(content)
    }

    private fun getContent(): String {
        return hlEditor.text.toString()
    }

    private fun setupHlEditor(){
        hlEditor.setHighlighter(textFormat.highlighter)
        textFormat.textModuleActions
                .setHighlightingEditor(hlEditor)
                .appendTextModuleActionsToBar(textModuleActionBar)
    }


    override fun dismiss(listner: DismissableAnimation.OnDismissedListener) {
        val revealAnim: RevealAnimationSettings = arguments?.getParcelable(ARG_REVEAL)!!
        startCircularExitAnimation(view!!,
                revealAnim,
                ContextCompat.getColor(context!!, R.color.white),
                ContextCompat.getColor(context!!, R.color.primary),
                object : DismissableAnimation.OnDismissedListener {
                    override fun onDismissed() {
                        listner.onDismissed()
                        view?.hideSoftKeyboard()
                    }

                })
    }

}