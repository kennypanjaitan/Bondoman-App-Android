package com.example.myapplication.ui.saveTransactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSaveTransactionsDialogBinding
import com.example.myapplication.models.FileFormat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SaveTransactionsDialog : BottomSheetDialogFragment() {
    private var _binding: FragmentSaveTransactionsDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var radioGroup: RadioGroup
    private lateinit var saveExtension: FileFormat
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save_transactions_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSaveTransactionsDialogBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        radioGroup = binding.saveRadioGroup
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.save_xls -> saveExtension = FileFormat.XLS
                R.id.save_xlsx -> saveExtension = FileFormat.XLSX
            }
        }
        binding.saveButton.setOnClickListener {
            saveAction()
        }
    }

    private fun saveAction() {
        Toast.makeText(requireContext(), "Saving Transactions...", Toast.LENGTH_SHORT).show()
        this.dismiss()
    }


}