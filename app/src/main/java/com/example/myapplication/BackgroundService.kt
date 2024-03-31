package com.example.myapplication


import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.example.myapplication.model.TokenModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException


class LoggingInterceptor: HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
                Log.i("LoggingInterceptor", "log: $message")
        }
}


class BackgroundService : Service() {

        private var handler: Handler? = null
        private val delay: Long = 4000

        private val runnable: Runnable = object : Runnable {
                override fun run() {
                        val sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
                        val token = sharedPreferences.getString("token", null)

                        sendPostRequest(token)

                        Log.d("TokenResponse", "Background Service berjalan... token = $token")
                        handler?.postDelayed(this, delay)
                }
        }

        private fun sendPostRequest(token:String?){
                Log.d("TokenResponse", "masuk function sendPostRequest")
                Log.d("TokenResponse", "token : $token")
                if(token != null){
                        val client = createOkHttpClient()
                        val url = "https://pbd-backend-2024.vercel.app/api/auth/token"
                        val formBody = FormBody.Builder().build()
//                        val body = RequestBody.create("text/plain".toMediaTypeOrNull(), "")

                        val request = Request.Builder()
                                .url(url)
                                .post(formBody)
                                .addHeader("Authorization", "Bearer ${token}")
                                .build()

                        client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException){
                                     Log.d("Service Error", e.toString())
                                }

                                override fun onResponse(call: Call, response: Response) {
                                        Log.d("TokenResponse", "responsecode : ${response.code}")
                                        Log.d("TokenResponse", response.body!!.string())
                                        if(response.isSuccessful){
                                                val responseData = response.body?.string()
                                                Log.d("TokenResponse", "responseService : $responseData")
                                        }
                                        else{
                                                Log.d("TokenResponse", "masuk onresponse tapi gak bisa apa2")
                                        }
                                }
                        })
                }
        }

        fun createOkHttpClient(): OkHttpClient {

                val peler = LoggingInterceptor()
                val loggingInterceptor = HttpLoggingInterceptor(peler)
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY // Set desired log level

                return OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .build()
        }
        override fun onCreate() {
                super.onCreate()
                handler = Handler(Looper.getMainLooper())
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
                handler?.post(runnable)
                return START_STICKY
        }

        override fun onBind(intent: Intent?): IBinder? {
                return null
        }

        override fun onDestroy() {
                handler?.removeCallbacks(runnable)
                super.onDestroy()
        }
}


