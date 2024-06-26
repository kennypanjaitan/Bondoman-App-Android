package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.models.LoginModel
import com.example.myapplication.retrofit.ApiService
import com.example.myapplication.retrofit.LoginResponse
import okhttp3.FormBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

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
//            val email = "13521001@std.stei.itb.ac.id"
//            val password = "password_13521001"
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

                            val sharedPreferences = getSharedPreferences("token", Context.MODE_PRIVATE)
                            sharedPreferences.edit().putString("token",
                                response.body()?.token.toString()
                            ).apply()

//                            send token to get expired date
                            val client = OkHttpClient()
                            val url = "https://pbd-backend-2024.vercel.app/api/auth/token"
                            val formBody = FormBody.Builder().build()

                            val request = Request.Builder()
                                .url(url)
                                .post(formBody)
                                .addHeader("Authorization", "Bearer ${token}")
                                .build()

                            client.newCall(request).enqueue(object : okhttp3.Callback {
                                override fun onFailure(call: okhttp3.Call, e: IOException){
                                }

                                override fun onResponse(call: okhttp3.Call, responseExp: okhttp3.Response) {
                                    if(responseExp.isSuccessful){
                                        val jsonResponse = JSONObject(responseExp.body!!.string())
                                        val expVal = jsonResponse.getLong("exp")


                                        val expTimePreferences = getSharedPreferences("expiredTokenDate", MODE_PRIVATE)
                                        val editor = expTimePreferences.edit()
                                        editor.putLong("expTime", expVal)
                                        editor.apply()

                                        setLoggedIn()
                                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                                        finish()
                                    }
                                    else{
                                        // .....
                                    }
                                }
                            })
                        }
                        else{
                            // .....
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    }

                })

        }
    }
    private fun setLoggedIn() {
        // Save the login status using SharedPreferences or any other suitable method
        val sharedPreferences = getSharedPreferences("login_status", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
//        startService(Intent(this, BackgroundService::class.java))
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }
}