package com.example.expensemanager.Network

import android.annotation.SuppressLint
import android.content.Context
import com.example.expensemanager.BuildConfig
import com.example.expensemanager.extensions.ExtensionMethods
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import org.apache.http.conn.ssl.SSLSocketFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

class RetrofitHelper(val context: Context) {
    val baseUrl = "http://serverlibrary-001-site1.gtempurl.com/api/"
    //http client to accept all ssl certificates
    fun OkHttpClient.Builder.ignoreAllSSLErrors(): OkHttpClient.Builder {
        val naiveTrustManager = object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
        }

        val insecureSocketFactory = SSLContext.getInstance("TLSv1.2").apply {
            val trustAllCerts = arrayOf<TrustManager>(naiveTrustManager)
            init(null, trustAllCerts, SecureRandom())
        }.socketFactory

        sslSocketFactory(insecureSocketFactory, naiveTrustManager)
        hostnameVerifier(HostnameVerifier { _, _ -> true })
        return this
    }
    fun getInstance(): Retrofit {
        val okHttpClient = OkHttpClient.Builder().apply {
            // ...
            if (BuildConfig.DEBUG) //if it is a debug build ignore ssl errors
                ignoreAllSSLErrors()
            //...
        }.build()
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}