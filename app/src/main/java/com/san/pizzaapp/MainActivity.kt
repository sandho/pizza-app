package com.san.pizzaapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.san.pizzaapp.model.Product
import com.san.pizzaapp.model.Products
import com.san.pizzaapp.utils.getAssetsJSON
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        lateinit var productAsString: String
        lateinit var product: Product
        lateinit var products: ArrayList<Product>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        productAsString = this.getAssetsJSON("pizzas.json") // get json data, from local assets
        product = Gson().fromJson(productAsString, Product::class.java) // converting string into json using Gson

        products = ArrayList<Product>() // converting product object into products arraylist for recyclerview
        products.add(product)

        Log.d(TAG, "onCreate: $products")
    }
}