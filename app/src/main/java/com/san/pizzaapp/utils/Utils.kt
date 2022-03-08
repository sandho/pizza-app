package com.san.pizzaapp.utils

import android.content.Context
import android.net.ConnectivityManager
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class Utils {

    companion object {
        val RUPESS_SYMBOL = "₹"
    }

    val BASEURL = "https://android.free.beeceptor.com/"
    val NETWORK_CALL: Boolean = false
}

fun String.setPriceWithRupeesSymbol(): String {
    return "${Utils.RUPESS_SYMBOL}$this"
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