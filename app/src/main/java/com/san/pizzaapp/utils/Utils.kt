package com.san.pizzaapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class Utils {

    companion object {
        val RUPESS_SYMBOL = "₹"
    }

    val BASEURL = "https://android.free.beeceptor.com/"

    // change false for access local json file, true for network call
    val NETWORK_CALL: Boolean = true
    val DB_NAME = "cart"
}

fun String.setPriceWithRupeesSymbol(): String {
    return "${Utils.RUPESS_SYMBOL}$this"
}

fun Context.isNetworkAvailable(): Boolean {
    var result = false

    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    connectivityManager.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            it!!.getNetworkCapabilities(connectivityManager!!.activeNetwork)?.apply {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        }

        return result
    }
}

fun Context.getAssetsJSON(fileName: String?): String {
    var json: String? = null

    try {
        val inputStream: InputStream = this.assets.open(fileName!!)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, Charset.forName("UTF-8"))
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return json!!
}