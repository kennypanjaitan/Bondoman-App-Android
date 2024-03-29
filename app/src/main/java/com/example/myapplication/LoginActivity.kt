// LoginActivity.kt
package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.model.LoginModel
import com.example.myapplication.retrofit.ApiService
import com.example.myapplication.retrofit.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        loginPost()

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
                setLoggedIn()

                // Navigate to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                // Close login activity
                finish()
            } else {
                // Display error message or handle invalid login
            }
        }
    }

    private fun loginPost() {
        val loginModel = LoginModel("13521004324324233211@std.stei.itb.ac.id", "password_13521001")
        ApiService.apiService.login(loginModel)
            .enqueue(object: Callback<LoginResponse>{
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>){
                    val responseText = "Response code : ${response.code()}\n" +
                            "token : ${response.body()?.token}" + "  --endtoken----"

                    printLog(responseText)
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable){
                    printLog(t.toString())
                }
            })
    }
    private fun printLog(message: String){
        Log.d("TokenResponse", message)
    }

    private fun isValidCredentials(email: String, password: String): Boolean {
        // Implement your login validation logic here
        // For demonstration purposes, assume login is successful if
        // email is not empty and password is "password"
        return email.isNotEmpty() && password == "password"
    }

    private fun setLoggedIn() {
        // Save the login status using SharedPreferences or any other suitable method
        val sharedPreferences = getSharedPreferences("login_status", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }
}
