package com.hobbajt.ynd.base.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class InternetConnectionInterceptor @Inject constructor(private val internetConnectionChecker: InternetConnectionChecker) : Interceptor
{
    @Throws(NoInternetConnectionException::class)
    override fun intercept(chain: Interceptor.Chain): Response
    {
        if(!internetConnectionChecker.isConnected())
        {
            throw NoInternetConnectionException()
        }
        return chain.proceed(chain.request())
    }
}