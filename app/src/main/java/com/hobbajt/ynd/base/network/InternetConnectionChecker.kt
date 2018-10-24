package com.hobbajt.ynd.base.network

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject


class InternetConnectionChecker @Inject constructor(private val context: Context)
{
    fun isConnected(): Boolean
    {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting && isOnline()
    }

    private fun isOnline(): Boolean
    {
        return try
        {
            val pingProcess = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com")
            val pingResult = pingProcess.waitFor()
            pingResult == 0
        } catch (e: Exception)
        {
            e.printStackTrace()
            false
        }

    }
}