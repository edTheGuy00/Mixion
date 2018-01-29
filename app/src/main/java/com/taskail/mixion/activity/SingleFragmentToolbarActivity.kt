package com.taskail.mixion.activity

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import com.taskail.mixion.R

/**
 *Created by ed on 1/19/18.
 */
abstract class SingleFragmentToolbarActivity<T : Fragment> : SingleFragmentActivity<T>()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(getToolbar())
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    protected fun getToolbar(): Toolbar {
        return findViewById(R.id.single_fragment_toolbar)
    }

    @LayoutRes
    override fun getLayout(): Int {
        return R.layout.activity_single_fragment_with_toolbar
    }

    protected fun hideToolbar(){
        supportActionBar?.hide()
    }

    protected fun showToolbar(){
        supportActionBar?.show()
    }

}