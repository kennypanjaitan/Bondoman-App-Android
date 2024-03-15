// LoginActivity.kt
package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // Perform login logic here
            if (isValidCredentials(email, password)) {
                // Update login status to true
                setLoggedIn(true)

                // Navigate to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Close login activity
            } else {
                // Display error message or handle invalid login
            }
        }
    }

    private fun isValidCredentials(email: String, password: String): Boolean {
        // Implement your login validation logic here
        // For demonstration purposes, assume login is successful if
        // email is not empty and password is "password"
        return email.isNotEmpty() && password == "password"
    }

    private fun setLoggedIn(isLoggedIn: Boolean) {
        // Save the login status using SharedPreferences or any other suitable method
        val sharedPreferences = getSharedPreferences("login_status", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }
}
