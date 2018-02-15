package com.taskail.mixion.activity

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.Log
import com.taskail.mixion.R
import com.taskail.mixion.utils.fadeInAnimation

/**
 *Created by ed on 1/19/18.
 */
abstract class SingleFragmentToolbarActivity<T : Fragment> : SingleFragmentActivity<T>()  {

    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(getToolbar())
        supportActionBar?.title = getString(R.string.app_name)
        Log.d(TAG, "onCreate")
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
        getToolbar().fadeInAnimation()
    }

}