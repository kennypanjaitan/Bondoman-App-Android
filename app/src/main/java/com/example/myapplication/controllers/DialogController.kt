package com.example.myapplication.controllers

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogController {
    private var dialog: AlertDialog? = null

    private fun cantShowDialog(): Boolean =
        dialog?.isShowing == true

    private fun dismissDialogs() {
        dialog?.dismiss()
        dialog = null
    }

    fun showDialogAlert(context: Context, title: String, message: String) {
        if (cantShowDialog()) { return }
        dialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Close") { _, _ -> this.dismissDialogs() }
            .setCancelable(false)
            .create()
            .apply {
                setCanceledOnTouchOutside(false)
                show()
            }
    }

    fun showDialogConfirmation(
        context: Context,
        title: String,
        message: String,
        positiveText: String,
        negativeText: String,
        callbackPos: () -> Unit,
        callbackNeg: (() -> Unit)?
    ) {
        if (cantShowDialog()) { return }
        dialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText) { _, _ -> callbackPos() }
            .setNegativeButton(negativeText) { _, _ ->
                if (callbackNeg != null) {
                    callbackNeg()
                }
                this.dismissDialogs()
            }
            .setCancelable(false)
            .create()
            .apply {
                setCanceledOnTouchOutside(false)
                show()
            }
    }

    fun showDialogSingleChoice(
        context: Context,
        title: String,
        items: Array<String>,
        callback: (Int) -> Unit
    ) {
        if (cantShowDialog()) { return }
        var checkedItem = 0
        dialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setSingleChoiceItems(items, checkedItem) { _, which -> checkedItem = which }
            .setPositiveButton("Confirm") { _, _ -> callback(checkedItem) }
            .setNegativeButton("Cancel") { _, _ -> this.dismissDialogs() }
            .setCancelable(false)
            .create()
            .apply {
                setCanceledOnTouchOutside(false)
                show()
            }
    }
}