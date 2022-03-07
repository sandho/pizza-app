package com.san.pizzaapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.gson.Gson
import com.san.pizzaapp.MainActivity
import com.san.pizzaapp.R
import com.san.pizzaapp.adapter.CartListAdapter
import com.san.pizzaapp.databinding.FragmentCartBinding
import com.san.pizzaapp.model.Product
import com.san.pizzaapp.model.ProductCart
import com.san.pizzaapp.room.CartDao
import com.san.pizzaapp.room.CartDatabase
import com.san.pizzaapp.utils.CartUpdateListener
import com.san.pizzaapp.utils.getAssetsJSON
import com.san.pizzaapp.utils.setPriceWithRupeesSymbol

class CartFragment(var mainActivity: MainActivity) : Fragment(), CartUpdateListener {

    private lateinit var binding: FragmentCartBinding

    companion object {
        private const val TAG = "CartFragment"
        lateinit var productAsString: String
        lateinit var product: Product
        lateinit var products: ArrayList<Product>
        lateinit var cartDao: CartDao
        lateinit var cartListAdapter: CartListAdapter
        var cartList: List<ProductCart> = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        val db = Room.databaseBuilder(
            requireContext(),
            CartDatabase::class.java, "cart"
        ).allowMainThreadQueries().build()

        cartListAdapter = CartListAdapter(this)

        cartDao = db.cartDao()
        cartList = cartDao.listAllCart()
        setCartList(cartList)

        val subTotal = cartList.sumOf {
            it.productPrice.toDouble() * it.productCartCount.toDouble()
        }

        binding.grandTotalTxt.text = subTotal.toString().setPriceWithRupeesSymbol()

        return binding.root
    }

    private fun setCartList(cartList: List<ProductCart>) {
        cartListAdapter.setCrustList(cartList)

        binding.cartListRecyclerView.adapter = cartListAdapter
        binding.cartListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun deleteCartItem(cartItemID: Int) {
        Toast.makeText(context, "delete - $cartItemID", Toast.LENGTH_SHORT).show()
        cartDao.cartItemDelete(cartItemID)
    }

    override fun updateCart(productCart: ProductCart, count: String, position: Int) {
        Toast.makeText(context, "${productCart.id} - $count", Toast.LENGTH_SHORT).show()

        if (count.toInt() >= 1) {
            cartDao.updateCartCount(count.toInt(), productCart.id)
        } else {
            cartList.toMutableList().removeAt(position)
            cartDao.cartItemDelete(productCart.id)
        }

        cartList = cartDao.listAllCart()
        setCartList(cartList)

        mainActivity.setBadge()
    }

}