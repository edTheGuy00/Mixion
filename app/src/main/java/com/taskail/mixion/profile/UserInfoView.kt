package com.taskail.mixion.profile

import android.content.res.Resources
import android.view.View
import android.widget.TextView
import com.taskail.mixion.R
import java.text.NumberFormat

/**
 *Created by ed on 3/7/18.
 */

class UserInfoView(val resources: Resources): ProfileContract.UserInfoView {

    override fun setFollowerCount(textView: TextView, count: Int?) {

        if (count != null)
            textView.text = resources.getQuantityString(R.plurals.follower_count,
                    count,
                    NumberFormat.getInstance().format(count))
    }

    override fun setFollowingCount(textView: TextView, count: Int?) {

        if (count != null)
            textView.text = resources.getQuantityString(R.plurals.following_count,
                    count,
                    NumberFormat.getInstance().format(count))
    }

    override fun setPostCount(textView: TextView, count: Int?) {

        if (count != null)
            textView.text = resources.getQuantityString(R.plurals.post_count,
                    count,
                    NumberFormat.getInstance().format(count))
    }

    override fun setBio(textView: TextView, about: String?) {

        if (!about.isNullOrEmpty())
        {
            textView.text = about
        }
        else
        {
            textView.visibility = View.GONE
        }
    }

    override fun setUserName(textView: TextView, name: String?, userName: String) {

        if (!name.isNullOrEmpty()){
            textView.text = name
        } else {
            textView.text = userName
        }
    }


}