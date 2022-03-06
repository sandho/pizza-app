package com.san.pizzaapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.san.pizzaapp.databinding.ActivityMainBinding
import com.san.pizzaapp.model.Product
import com.san.pizzaapp.ui.CustomizeProductBottomSheet
import com.san.pizzaapp.utils.getAssetsJSON

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        lateinit var productAsString: String
        lateinit var product: Product
        lateinit var products: ArrayList<Product>
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productAsString = this.getAssetsJSON("pizzas.json") // get json data, from local assets
        product = Gson().fromJson(productAsString, Product::class.java) // converting string into json using Gson

        products = ArrayList() // converting product object into products arraylist for recyclerview
        products.add(product)

        setProductIntoView(product)

        Log.d(TAG, "onCreate: $products")
    }

    private fun setProductIntoView(product: Product?) {
        binding.productNameTxt.text = product!!.name
        binding.productDescTxt.text = product!!.description

        if (product.isVeg) {
            binding.productIsVeg.setImageResource(R.drawable.ic_veg_icon)
        }

        val bottomSheet = CustomizeProductBottomSheet(product)

        // opening bottom sheet for add custom product to cart
        binding.addBtn.setOnClickListener {
            bottomSheet.show(
                supportFragmentManager,
                bottomSheet.tag
            )
        }
    }
}