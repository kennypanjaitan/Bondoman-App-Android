package com.example.myapplication.ui.saveTransactions

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.controllers.DialogController
import com.example.myapplication.controllers.FileController
import com.example.myapplication.controllers.SpreadsheetController
import com.example.myapplication.databinding.FragmentSaveTransactionsDialogBinding
import com.example.myapplication.models.FileFormat
import com.example.myapplication.ui.transaction.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SaveTransactionsDialog : BottomSheetDialogFragment() {
    private var _binding: FragmentSaveTransactionsDialogBinding? = null
    private val binding get() = _binding!!
    private val _createFileCode = 1
    private val transactionViewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_save_transactions_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSaveTransactionsDialogBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        var clicked = false
        binding.apply {
            saveRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.save_xls -> FileController.setFileFormat(FileFormat.XLS)
                    R.id.save_xlsx -> FileController.setFileFormat(FileFormat.XLSX)
                }
                clicked = true
            }
            saveButton.setOnClickListener {
                if (clicked) {
                    startActivityForResult(FileController.createFileIntent(), _createFileCode)
                } else {
                    Toast.makeText(requireContext(), "Please select file format", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                _createFileCode -> data?.data?.also { uri ->
                    FileController.setUri(uri)
                    saveAction(uri)
                }
            }
        }
    }

    private fun saveAction(uri: Uri) {
        val context = requireContext()
        Toast.makeText(context, "Saving Transactions...", Toast.LENGTH_SHORT).show()
        SpreadsheetController.writeSpreadsheet(requireContext(), uri, transactionViewModel.listTransaction.value!!)
        sendEmail(context, uri)
    }

    private fun sendEmail(context: Context, uri: Uri) {
        DialogController.showDialogConfirmation(
            context,
            "Send Transactions Data via Email",
            "Do you want to send the transactions data via email?",
            "Yes",
            "No",
            { FileController.sendFileIntentViaEmail(context, uri) },
            { this.dismiss() }
        )
    }
}