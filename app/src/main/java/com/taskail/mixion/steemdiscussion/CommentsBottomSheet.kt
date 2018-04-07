package com.taskail.mixion.steemdiscussion

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.DefaultItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.User
import com.taskail.mixion.data.models.ContentReply
import kotlinx.android.synthetic.main.bottom_sheet_comments.*
import kotlinx.android.synthetic.main.item_parent_comment.view.*


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
    var hasReplies = false

    private lateinit var parentComment: View

    private lateinit var commentsAdapter: CommentsRecyclerViewAdapter


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?):
            View? {

        parentComment = layoutInflater.inflate(R.layout.item_parent_comment,
                commentsRecycler, false)

        return inflater.inflate( R.layout.bottom_sheet_comments, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repliesToolbar.setNavigationOnClickListener {
            dismiss()
        }

        btn_send.setOnClickListener {
            if (!commentInput.text.isNullOrEmpty()){
                presenter.postCommentReply(commentAuthor, commentPermLink, commentInput.text.toString())

                btn_send.isClickable = false
            }
        }

        if (!User.userIsLoggedIn){
            input_bar.visibility = View.GONE
        }

        setParentComment()

        commentsAdapter = CommentsRecyclerViewAdapter(parentComment)

        commentsRecycler.itemAnimator = DefaultItemAnimator()
        commentsRecycler.adapter = commentsAdapter

    }

    override fun displayComments(commentsFromResponse: Array<ContentReply>) {
        commentsAdapter.addComments(commentsFromResponse)
    }

    private fun setParentComment() {
        parentComment.userName.text = commentAuthor
        parentComment.commentBody.text = commentContent
        commentInput.hint = "reply to $commentAuthor"
    }

}