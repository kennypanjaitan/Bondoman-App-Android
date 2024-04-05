package com.example.myapplication


import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.example.myapplication.model.TokenModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import io.jsonwebtoken.Jwts
import java.time.Instant
import java.util.*
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager


class BackgroundService : Service() {

        private var handler: Handler? = null
        private val delay: Long = 5000 // set delay to hit endpoint

        private val runnable: Runnable = object : Runnable {
                override fun run() {
                        val sharedPreferences = getSharedPreferences("expiredTokenDate", MODE_PRIVATE)
                        val expiredTime = sharedPreferences.getLong("expTime", 0)

//                        sendPostRequest(token)

                        val currentTimeSeconds = System.currentTimeMillis() / 1000

                        if(currentTimeSeconds > expiredTime){
                                val logOutPreferences = getSharedPreferences("login_status", MODE_PRIVATE)
                                val editor = logOutPreferences.edit()
                                editor.putBoolean("isLoggedIn", false)
                                editor.apply()

                                // delete all preferences
                                val allPreferences =  PreferenceManager.getDefaultSharedPreferences(applicationContext)
                                allPreferences.edit().clear().apply()

                                val intent = Intent(applicationContext, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                                stopSelf()

                        }
                        else {

                        }


                        handler?.postDelayed(this, delay)
                }
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


