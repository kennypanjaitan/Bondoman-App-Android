// MainActivity.kt
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.myapplication.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.PieChart
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    lateinit var goPieChart: Button
    
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goPieChart = findViewById(R.id.pie_chart)

        goPieChart.setOnClickListener{
            startActivity(Intent(this@MainActivity,Statistics::class.java))
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_transactions, R.id.navigation_scan, R.id.navigation_statistics, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Check login status
        if (!isLoggedIn()) {
            navigateToLogin()
        }
    }

    private fun isLoggedIn(): Boolean {
        // Retrieve login status from SharedPreferences
        val sharedPreferences = getSharedPreferences("login_status", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Close MainActivity
    }
}
