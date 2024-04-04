package com.example.myapplication.controllers

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogController {
    private lateinit var dialogAlert: AlertDialog
    private lateinit var dialogConfirmation: AlertDialog

    private fun cantShowDialog(): Boolean =
        (this::dialogAlert.isInitialized && dialogAlert.isShowing) ||
                (this::dialogConfirmation.isInitialized && dialogConfirmation.isShowing)

    fun showDialogAlert(context: Context, title: String, message: String) {
        if (cantShowDialog()) { return }
        dialogAlert = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Close", null)
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
        dialogConfirmation = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText) { _, _ -> callbackPos() }
            .setNegativeButton(negativeText) { _, _ ->
                if (callbackNeg != null) {
                    callbackNeg()
                }
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
        dialogAlert = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setSingleChoiceItems(items, checkedItem) { _, which -> checkedItem = which }
            .setPositiveButton("Confirm") { _, _ -> callback(checkedItem) }
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
            .create()
            .apply {
                setCanceledOnTouchOutside(false)
                show()
            }
    }
}