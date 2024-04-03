package com.example.myapplication.controllers

import android.content.Context
import android.net.ConnectivityManager
import com.example.myapplication.models.ConnectivityObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkObserver(
    private val context: Context
) : ConnectivityObserver {

    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: android.net.Network) {
                    trySend(ConnectivityObserver.Status.Available).isSuccess
                }

                override fun onLost(network: android.net.Network) {
                    trySend(ConnectivityObserver.Status.Lost).isSuccess
                }

                override fun onUnavailable() {
                    trySend(ConnectivityObserver.Status.Unavailable).isSuccess
                }

                override fun onLosing(network: android.net.Network, maxMsToLive: Int) {
                    trySend(ConnectivityObserver.Status.Losing).isSuccess
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
        }.distinctUntilChanged()
    }
}