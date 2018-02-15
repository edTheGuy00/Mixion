package com.taskail.mixion.ui

import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher

/**
 *Created by ed on 2/14/18.
 */

class TextInputValidator(val validationChanged: (Boolean) -> Unit,
                         vararg val textInputs: TextInputLayout) {

    init {
        val triggerWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun afterTextChanged(editable: Editable) {
                revalidate()
            }
        }

        for (t in textInputs) {
            t.editText?.addTextChangedListener(triggerWatcher)
        }
    }

    private var lastIsValidValue = false

    private fun revalidate() {
        var isValid = true
        for (t in textInputs) {
            isValid = isValid && t.editText!!.text.isNotEmpty()
        }

        if (isValid != lastIsValidValue) {
            lastIsValidValue = isValid
            validationChanged(isValid)
        }
    }

}