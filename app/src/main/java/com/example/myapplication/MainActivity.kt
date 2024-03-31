// MainActivity.kt
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.myapplication.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.retrofit.LoginResponse


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        val navController = navHostFragment!!.findNavController()

        // run service
//        startService(Intent(this, BackgroundService::class.java))

        // Check login status

        if (!isLoggedIn()) {
            Log.d("TokenResponse", "notLoggedIn")

            val sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
            var token = sharedPreferences.getString("token", null)

            Log.d("TokenResponse", "token sebelum login : " + token)

            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)


        }
        else {
            val sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
            val updatedToken = sharedPreferences.getString("token", null)

            Log.d("TokenResponse", "token setelah login : " + updatedToken)

            Log.d("TokenResponse", "isLoggedIn")
        }


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
}
