// MainActivity.kt
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.myapplication.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.retrofit.LoginResponse
import com.example.myapplication.controllers.SnackbarController

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private lateinit var snackBarController: SnackbarController
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        val navController = navHostFragment!!.findNavController()

        // run service
        startService(Intent(this, BackgroundService::class.java))

        // Check login status

        if (!isLoggedIn()) {
            val sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)

            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        else {
            val sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
            val updatedToken = sharedPreferences.getString("token", null)
        }


        // Set up Network Snack bar
        snackBarController = SnackbarController
        snackBarController.init(this)
        snackBarController.observeStatus(binding.root, lifecycleScope)

        // Set up navigation
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_transactions, R.id.navigation_scan, R.id.navigation_statistics, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("login_status", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
