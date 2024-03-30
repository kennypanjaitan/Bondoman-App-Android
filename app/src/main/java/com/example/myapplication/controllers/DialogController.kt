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
        callback: () -> Unit
    ) {
        if (cantShowDialog()) { return }
        dialogConfirmation = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Confirm") { _, _ -> callback() }
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
            .create()
            .apply {
                setCanceledOnTouchOutside(false)
                show()
            }
    }
}