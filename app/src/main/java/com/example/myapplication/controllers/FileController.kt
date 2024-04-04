package com.example.myapplication.controllers

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication.models.FileFormat

object FileController {
    private lateinit var fileFormat: FileFormat
    private var uri: Uri? = null

    fun setUri(uri: Uri) {
        this.uri = uri
    }

    fun setFileFormat(fileFormat: FileFormat) {
        this.fileFormat = fileFormat
    }

    fun createFileIntent(): Intent {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = fileFormat.mimeType
            putExtra(Intent.EXTRA_TITLE, "TransactionsBondoMan.${fileFormat.extString}")
            putExtra(
                DocumentsContract.EXTRA_INITIAL_URI,
                Uri.parse("content://com.android.externalstorage.documents/document/primary:Documents"))
        }

        return intent
    }

    fun pickFileIntent(): Intent {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(DocumentsContract.EXTRA_INITIAL_URI,
                Uri.parse("content://com.android.externalstorage.documents/document/primary:Documents")
            )
            putExtra(Intent.EXTRA_TITLE, "TransactionsBondoMan.xls")
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(FileFormat.XLS.mimeType, FileFormat.XLSX.mimeType))
            type = "application/vnd.*"
        }

        return intent
    }

    fun sendFileIntentViaEmail(context: Context, fileUri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_SUBJECT, "Transaction Data")
            putExtra(Intent.EXTRA_EMAIL, arrayListOf("earlvonraven@gmail.com"))
            putExtra(
                Intent.EXTRA_TEXT,
                "All the transactions data from BondoMan attached in this email."
            )
            putExtra(
                Intent.EXTRA_STREAM,
                fileUri
            )
            type = "message/rfc822"
        }

        try {
            startActivity(context, intent, null)
        } catch (e: ActivityNotFoundException) {
            DialogController.showDialogAlert(
                context,
                "Error",
                "No email app found, please install one first"
            )
        }
    }
}