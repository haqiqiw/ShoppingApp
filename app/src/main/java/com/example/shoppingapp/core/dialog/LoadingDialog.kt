package com.example.shoppingapp.core.dialog

import android.app.AlertDialog
import android.content.Context
import android.widget.FrameLayout
import com.example.shoppingapp.R

class LoadingDialog(context: Context) {

    private var dialog: AlertDialog? = null
    private var isShown: Boolean = false

    init {
        val builder = AlertDialog.Builder(context, R.style.WrapContentDialog)
        val rootView = FrameLayout(context)
        builder.setView(rootView)
        builder.setCancelable(false)

        dialog = builder.create()
        dialog?.layoutInflater?.inflate(R.layout.loading_dialog, rootView)
    }

    fun show() {
        dialog?.let {
            isShown = true
            it.show()
        }
    }

    fun dismiss() {
        if (!isShown) return
        dialog?.dismiss()
    }
}
