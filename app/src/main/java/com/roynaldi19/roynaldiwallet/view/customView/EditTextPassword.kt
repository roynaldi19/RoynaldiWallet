package com.roynaldi19.roynaldiwallet.view.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.roynaldi19.roynaldiwallet.R

class EditTextPassword : AppCompatEditText {
    private var length = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                length = s.length
                error =
                    if (length < 6) context.getString(R.string.validasi_password) else null
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
}