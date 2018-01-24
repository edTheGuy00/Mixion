package com.taskail.mixion.activity

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.taskail.mixion.R

/**
 *Created by ed on 1/19/18.
 */

abstract class SingleFragmentActivity<T : Fragment>: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(getLayout())

        if (!isFragmentCreated()) {
            addFragment(createFragment())
        }
    }

    protected fun addFragment(fragment: T) {
        supportFragmentManager.beginTransaction().add(getContainerId(), fragment).commit()
    }

    protected fun getFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(getContainerId())
    }

    protected abstract fun createFragment(): T

    @LayoutRes
    protected open fun getLayout() : Int{
        return R.layout.activity_single_fragment
    }
    @IdRes
    protected fun getContainerId() : Int{
        return R.id.fragment_container
    }

    protected fun isFragmentCreated(): Boolean {
        return getFragment() != null
    }
}