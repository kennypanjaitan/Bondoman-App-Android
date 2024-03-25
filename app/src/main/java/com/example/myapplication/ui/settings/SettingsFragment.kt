package com.example.myapplication.ui.settings

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.myapplication.LoginActivity
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

        // Navigate to LoginActivity and destroy MainActivity
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    private fun onClickSave() {
        SaveTransactionsDialog().show(requireActivity().supportFragmentManager, "SaveTransactionsDialog")
    }
}