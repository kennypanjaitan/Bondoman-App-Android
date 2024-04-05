package com.example.myapplication.controllers

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.myapplication.models.ConnectivityObserver
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object SnackbarController {
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityObserver: ConnectivityObserver
    private var isNetworkAvailable = true

    private fun isSnackbarShown(): Boolean =
        this::snackbar.isInitialized && snackbar.isShown

    fun init(context: Context) {
        this.connectivityObserver = NetworkObserver(context)
    }

    fun observeStatus(view: View, lifecycleScope: LifecycleCoroutineScope) {
        connectivityObserver.observe().onEach {
            when (it) {
                ConnectivityObserver.Status.Available -> {
                    if (!isNetworkAvailable) {
                        showSnackbar(view, "Network available", Snackbar.LENGTH_SHORT)
                        isNetworkAvailable = true
                    }
                }
                ConnectivityObserver.Status.Unavailable -> {
                    showSnackbar(view, "Network unavailable", Snackbar.LENGTH_INDEFINITE)
                    isNetworkAvailable = false
                }
                ConnectivityObserver.Status.Losing -> {
                    showSnackbar(view, "Network losing", Snackbar.LENGTH_INDEFINITE)
                    isNetworkAvailable = false
                }
                ConnectivityObserver.Status.Lost -> {
                    showSnackbar(view, "Network lost", Snackbar.LENGTH_INDEFINITE)
                    isNetworkAvailable = false
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun showSnackbar(view: View, message: String, duration: Int) {
        if (isSnackbarShown()) { snackbar.dismiss() }
        snackbar = Snackbar.make(view, message, duration).apply {
            setAction("Close") { dismiss() }
            show()
        }
    }
}