package com.taskail.mixion.steemdiscussion

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.steemJ.RxSteemJManager
import com.taskail.mixion.steemJ.SteemJCallback
import kotlinx.android.synthetic.main.bottom_sheet_comments.*


/**
 *Created by ed on 3/26/18.
 */

class CommentsBottomSheet: BottomSheetDialogFragment(), DiscussionContract.BottomSheetView {

    override lateinit var presenter: DiscussionContract.Presenter

    companion object {

        fun newInstance(): CommentsBottomSheet {
            return CommentsBottomSheet()
        }
    }

    lateinit var commentAuthor: String
    lateinit var commentContent: String
    lateinit var commentPermLink: String


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?):
            View? {
        return inflater.inflate( R.layout.bottom_sheet_comments, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repliesToolbar.setNavigationOnClickListener {
            dismiss()
        }

        btn_send.setOnClickListener {
            if (!commentInput.text.isNullOrEmpty()){
                presenter.postReply(commentAuthor, commentPermLink, commentInput.text.toString())

                btn_send.isClickable = false
            }
        }

        setParentComment()
    }

    private fun setParentComment() {
        userName.text = commentAuthor
        commentBody.text = commentContent

        commentInput.hint = "reply to $commentAuthor"
    }

}