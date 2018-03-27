package com.taskail.mixion.steemdiscussion

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import kotlinx.android.synthetic.main.bottom_sheet_comments.*


/**
 *Created by ed on 3/26/18.
 */

class CommentsBottomSheet: BottomSheetDialogFragment() {

    companion object {

        const val AUTHOR = "author"
        const val BODY = "body"

        fun newInstance(author: String, body: String): CommentsBottomSheet {
            val bundle = Bundle()
            bundle.putString(AUTHOR, author)
            bundle.putString(BODY, body)
            val fragment = CommentsBottomSheet()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?):
            View? {
        return inflater.inflate( R.layout.bottom_sheet_comments, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val author = arguments?.getString(AUTHOR)
        val body = arguments?.getString(BODY)

        if (author != null && body != null) {
            setParentComment(author, body)
        }
    }

    private fun setParentComment(author: String?, body: String?) {
        userName.text = author
        commentBody.text = body
    }

}