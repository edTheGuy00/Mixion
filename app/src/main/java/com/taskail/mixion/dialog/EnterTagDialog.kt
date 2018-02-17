package com.taskail.mixion.dialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialog
import com.taskail.mixion.R
import com.taskail.mixion.ui.TextInputValidator
import kotlinx.android.synthetic.main.dialog_enter_tag.*

/**
 *Created by ed on 2/17/18.
 */

class EnterTagDialog(context: Context,
                     private val addTag: (String) -> Unit) : AppCompatDialog(context){

    init {
        setContentView(R.layout.dialog_enter_tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TextInputValidator({
            submitTagBtn.isEnabled = it
        }, tag_input)

        submitTagBtn.setOnClickListener {
            addTag(getTag())
            this.dismiss()
        }
    }

    private fun getTag(): String{
        return tag_input.editText?.text.toString().toLowerCase()
    }
}