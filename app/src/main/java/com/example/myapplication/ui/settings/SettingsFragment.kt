package com.example.myapplication.ui.settings

import android.app.Activity.RESULT_OK
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.myapplication.R
import com.example.myapplication.controllers.DialogController
import com.example.myapplication.controllers.FileController
import com.example.myapplication.ui.saveTransactions.SaveTransactionsDialog

class SettingsFragment : PreferenceFragmentCompat() {
    private val _openDocumentCode = 2

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            "logout" -> onClickLogout()
            "save_transactions" -> onClickSave()
            "send_transactions" -> onClickSend()
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun onClickLogout() {
        DialogController.showDialogConfirmation(
            requireContext(),
            "Logout",
            "Are you sure you want to logout?",
            "Confirm",
            "Cancel",
            this::logoutUser,
            null
        )
    }

    private fun onClickSave() {
        SaveTransactionsDialog().show(
            requireActivity().supportFragmentManager,
            "SaveTransactionsDialog"
        )
    }

    private fun onClickSend() {
        DialogController.showDialogSingleChoice(
            requireContext(),
            "Send Transactions Data via Email",
            arrayOf("Create a new file", "Select an existing file"),
        ) { which ->
            when (which) {
                0 -> onClickSave()
                1 -> startActivityForResult(FileController.pickFileIntent(), _openDocumentCode)
            }
        }
    }

    private fun logoutUser() {
        // show toast
        Toast.makeText(requireContext(), "Logging out...", Toast.LENGTH_SHORT).show()

        // Update login status to false
        val sharedPreferences = requireActivity().getSharedPreferences("login_status", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        findNavController().navigate(R.id.loginActivity)
        requireActivity().finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == _openDocumentCode && resultCode == RESULT_OK) {
            data?.data?.also { uri ->
                FileController.sendFileIntentViaEmail(requireContext(), uri)
            }
        }
    }
}
