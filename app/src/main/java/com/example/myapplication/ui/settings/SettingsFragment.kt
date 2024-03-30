package com.example.myapplication.ui.settings

import android.content.ActivityNotFoundException
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.myapplication.R
import com.example.myapplication.controllers.DialogController
import com.example.myapplication.ui.saveTransactions.SaveTransactionsDialog

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            "logout" -> DialogController.showDialogConfirmation(
                requireContext(),
                "Logout",
                "Are you sure you want to logout?",
                this::onClickLogout
            )
            "save_transactions" -> onClickSave()
            "send_transactions" -> onClickSend()
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun onClickLogout() {
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

    private fun onClickSave() {
        SaveTransactionsDialog().show(requireActivity().supportFragmentManager, "SaveTransactionsDialog")
    }

    private fun onClickSend() {
        /**
         * TODO:
         * 1. Change action to SEND, for sending attachment
         * 2. Solve problem: Gmail not found as email client when ACTION_SEND
         */
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, "Transaction Data")
            putExtra(Intent.EXTRA_EMAIL, "13521023@std.stei.itb.ac.id")
            putExtra(Intent.EXTRA_TEXT, "This is all the transactions data from BondoMan")
        }

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            DialogController.showDialogAlert(
                requireContext(),
                "Error",
                "No email app found, please install one first"
            )
        }
    }
}