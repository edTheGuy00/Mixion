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

class CommentsBottomSheet: BottomSheetDialogFragment() {

    companion object {

        const val AUTHOR = "author"
        const val BODY = "body"
        const val PERMLINK = "permlink"

        fun newInstance(author: String,
                        body: String,
                        permLink: String): CommentsBottomSheet {
            val bundle = Bundle()
            bundle.putString(AUTHOR, author)
            bundle.putString(BODY, body)
            bundle.putString(PERMLINK, permLink)
            val fragment = CommentsBottomSheet()
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var commentAuthor: String
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

        val author = arguments?.getString(AUTHOR)
        val body = arguments?.getString(BODY)
        val permlink = arguments?.getString(PERMLINK)

        if (author != null && body != null && permlink != null) {
            setParentComment(author, body)
            commentAuthor = author
            commentPermLink = permlink
        }

        btn_send.setOnClickListener {
            if (!commentInput.text.isNullOrEmpty()){
                postReply(commentInput.text.toString())

                btn_send.isClickable = false
            }
        }
    }

    private fun setParentComment(author: String?, body: String?) {
        userName.text = author
        commentBody.text = body

        commentInput.hint = "reply to $author"
    }

    private fun postReply(content: String) {
        val tags = arrayOf("mixion", "test")
        RxSteemJManager.comment(commentAuthor, commentPermLink, content, tags,
                object : SteemJCallback.CreatePostCallBack {
                    override fun onSuccess(permLink: String) {
                        dismiss()
                    }

                    override fun onError(e: Throwable) {
                        btn_send.isClickable = true
                    }

                })
    }

}