package com.taskail.mixion.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.taskail.mixion.R
import com.taskail.mixion.ui.TextInputValidator
import kotlinx.android.synthetic.main.dialog_enter_tag.*

/**
 *Created by ed on 2/17/18.
 */

class EnterTagDialog(context: Context,
                     private val addTag: (String) -> Unit) :
        Dialog(context){

    init {
        setContentView(R.layout.dialog_enter_tag)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
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