package com.san.pizzaapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.gson.Gson
import com.san.pizzaapp.adapter.CartListAdapter
import com.san.pizzaapp.databinding.ActivityMainBinding
import com.san.pizzaapp.model.Crust
import com.san.pizzaapp.model.CrustSize
import com.san.pizzaapp.model.Product
import com.san.pizzaapp.model.ProductCart
import com.san.pizzaapp.room.CartDao
import com.san.pizzaapp.room.CartDatabase
import com.san.pizzaapp.ui.CustomizeProductBottomSheet
import com.san.pizzaapp.utils.CartUpdateListener
import com.san.pizzaapp.utils.OnClickListener
import com.san.pizzaapp.utils.getAssetsJSON

class MainActivity : AppCompatActivity(), CartUpdateListener {

    companion object {
        private const val TAG = "MainActivity"
        lateinit var productAsString: String
        lateinit var product: Product
        lateinit var products: ArrayList<Product>
        lateinit var cartDao: CartDao
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

        val db = Room.databaseBuilder(
            this,
            CartDatabase::class.java, "cart"
        ).allowMainThreadQueries().build()

        cartDao = db.cartDao()
        val cartList: List<ProductCart> = cartDao.listAllCart()

        setCartList(cartList)
        var subTotal = cartList.sumOf {
            it.productPrice.toDouble()
        }

        Log.d(TAG, "onCreate subTotal: $products - $subTotal")
    }

    private fun setCartList(cartList: List<ProductCart>) {
        val cartListAdapter = CartListAdapter(this)
        cartListAdapter.setCrustList(cartList)

        binding.cartListRecyclerView.adapter = cartListAdapter
        binding.cartListRecyclerView.layoutManager = LinearLayoutManager(this)
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

    override fun deleteCartItem(cartItemID: Int) {
        Toast.makeText(this, "delete - $cartItemID", Toast.LENGTH_SHORT).show()
        cartDao.cartItemDelete(cartItemID)
    }

    override fun updateCart(productCart: ProductCart, count: String) {
        Toast.makeText(this, "${productCart.id} - $count", Toast.LENGTH_SHORT).show()

        if (count.toInt() >= 1) {
            cartDao.updateCartCount(count.toInt(), productCart.id)
        } else {
            cartDao.cartItemDelete(productCart.id)
        }
    }

}