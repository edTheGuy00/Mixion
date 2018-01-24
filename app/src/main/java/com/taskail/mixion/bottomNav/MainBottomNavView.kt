package com.taskail.mixion.bottomNav

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import com.taskail.mixion.R
import com.taskail.mixion.favorites.BookmarkFragment
import com.taskail.mixion.feed.FeedFragment

/**
 *Created by ed on 1/24/18.
 */
enum class MainBottomNavView(@StringRes private var text: Int, @DrawableRes private var icon: Int) : EnumCode {

    FEED(R.string.feed, R.drawable.ic_feed) {
        override fun newInstance(): Fragment {
            return FeedFragment.newInstance()
        }
    },
    BOOKMARK(R.string.bookmark, R.drawable.ic_bookmark_border_black_24dp){
        override fun newInstance(): Fragment {
            return BookmarkFragment.newInstance()
        }
    };

    companion object {

        @JvmStatic private val MAP = EnumCodeMap(MainBottomNavView::class.java)

        @JvmStatic fun size(): Int {
            return MAP.size()
        }

        @JvmStatic fun of(code: Int): MainBottomNavView {
            return MAP[code]
        }
    }

    @StringRes
    fun text(): Int {
        return text
    }

    @DrawableRes
    fun icon(): Int {
        return icon
    }

    abstract fun newInstance(): Fragment

    override fun code(): Int {
        return ordinal
    }
}