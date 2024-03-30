// LoginActivity.kt
package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.model.LoginModel
import com.example.myapplication.retrofit.ApiService
import com.example.myapplication.retrofit.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
//            val email = editTextEmail.text.toString()
//            val password = editTextPassword.text.toString()
            val email = "13521001@std.stei.itb.ac.id"
            val password = "password_13521001"
            val loginModel = LoginModel(email,password)

            ApiService.apiService.login(loginModel)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if(response.isSuccessful){
                            val status = response.code()
                            val token = response.body()?.token
                            printLog(
                                "status : " + status +
                                        "token : " + response.body()?.token
                            )
                            val sharedPreferences = getSharedPreferences("token", Context.MODE_PRIVATE)
                            sharedPreferences.edit().putString("token",
                                response.body()?.token.toString()
                            ).apply()



//                            setLoggedIn()
//                            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
//                            val navController = navHostFragment!!.findNavController()
//                            navController.navigate(R.id.container)

                            setLoggedIn()
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                        else{
                            if(response.code() == 401){

                            }
                            // .....
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        printLog(t.toString())
                    }

                })

        }
    }
    private fun printLog(message: String){
        Log.d("TokenResponse", message)
    }
    private fun setLoggedIn() {
        // Save the login status using SharedPreferences or any other suitable method
        val sharedPreferences = getSharedPreferences("login_status", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }
}
