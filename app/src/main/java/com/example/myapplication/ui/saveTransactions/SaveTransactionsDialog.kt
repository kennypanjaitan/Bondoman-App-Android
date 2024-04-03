package com.example.myapplication.ui.saveTransactions

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.controllers.SpreadsheetController
import com.example.myapplication.databinding.FragmentSaveTransactionsDialogBinding
import com.example.myapplication.models.FileFormat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SaveTransactionsDialog : BottomSheetDialogFragment() {
    private var _binding: FragmentSaveTransactionsDialogBinding? = null
    private val binding get() = _binding!!
    private val CREATE_FILE = 1
    private lateinit var saveExtension: FileFormat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_save_transactions_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSaveTransactionsDialogBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            saveRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.save_xls -> saveExtension = FileFormat.XLS
                    R.id.save_xlsx -> saveExtension = FileFormat.XLSX
                }
            }
            saveButton.setOnClickListener { createFile() }
        }
    }

    private fun createFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = saveExtension.mimeType
            putExtra(Intent.EXTRA_TITLE, "TransactionsBondoMan.${saveExtension.extString}")
            putExtra(DocumentsContract.EXTRA_INITIAL_URI,
                Uri.parse("content://com.android.externalstorage.documents/document/primary:Documents"))
        }
        startActivityForResult(intent, CREATE_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CREATE_FILE -> data?.data?.also { uri ->
                    saveAction(uri)
                }
            }
        }
    }

    private fun saveAction(uri: Uri) {
        Toast.makeText(requireContext(), "Saving Transactions...", Toast.LENGTH_SHORT).show()
        SpreadsheetController.writeSpreadsheet(requireContext(), uri)
        this.dismiss()
    }


}