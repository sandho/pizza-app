package com.san.pizzaapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.san.pizzaapp.MainActivity
import com.san.pizzaapp.R
import com.san.pizzaapp.adapter.CartListAdapter
import com.san.pizzaapp.databinding.FragmentCartBinding
import com.san.pizzaapp.model.ProductCart
import com.san.pizzaapp.room.CartDao
import com.san.pizzaapp.room.CartDatabase
import com.san.pizzaapp.utils.CartUpdateListener
import com.san.pizzaapp.utils.Utils
import com.san.pizzaapp.utils.setPriceWithRupeesSymbol

class CartFragment(var mainActivity: MainActivity) : Fragment(), CartUpdateListener {

    private lateinit var binding: FragmentCartBinding

    companion object {
        lateinit var cartDao: CartDao
        lateinit var cartListAdapter: CartListAdapter
        var cartList: List<ProductCart> = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        val db = Room.databaseBuilder(
            requireContext(),
            CartDatabase::class.java, Utils().DB_NAME
        ).allowMainThreadQueries().build()

        cartListAdapter = CartListAdapter(this)

        cartDao = db.cartDao()
        cartList = cartDao.listAllCart()
        setCartList(cartList)

        setGrandTotal()
        checkCartIsEmpty()

        return binding.root
    }

    private fun setGrandTotal() {
        val subTotal = cartList.sumOf {
            it.productPrice.toDouble() * it.productCartCount.toDouble()
        }

        binding.grandTotalTxt.text = subTotal.toString().setPriceWithRupeesSymbol()
    }

    private fun setCartList(cartList: List<ProductCart>) {
        cartListAdapter.setCrustList(cartList)

        binding.cartListRecyclerView.adapter = cartListAdapter
        binding.cartListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun deleteCartItem(cartItemID: Int, position: Int) {
        cartList.toMutableList().removeAt(position)
        cartDao.cartItemDelete(cartItemID)

        cartList = cartDao.listAllCart()
        setCartList(cartList)

        mainActivity.setBadge() // update count on bottom nav badge
        setGrandTotal()

        checkCartIsEmpty()

        Toast.makeText(context, "Cart Removed", Toast.LENGTH_SHORT).show()
    }

    override fun updateCart(productCart: ProductCart, count: String, position: Int) {
        if (count.toInt() >= 1) {
            cartDao.updateCartCount(count.toInt(), productCart.id)
        } else {
            cartList.toMutableList().removeAt(position)
            cartDao.cartItemDelete(productCart.id)
        }

        cartList = cartDao.listAllCart()
        setCartList(cartList)

        checkCartIsEmpty()

        mainActivity.setBadge() // update count on bottom nav badge

        Toast.makeText(context, "Cart Updated", Toast.LENGTH_SHORT).show()
    }

    fun checkCartIsEmpty() {
        if (cartList.count() >= 1) {
            setGrandTotalCart()
            setGrandTotal()
        } else {
            setEmptyCart()
        }
    }

    fun setEmptyCart() {
        binding.grandTotalTitleTxt.text = getString(R.string.cart_empty)
        binding.grandTotalTxt.visibility = View.GONE
    }

    fun setGrandTotalCart() {
        binding.grandTotalTitleTxt.text = getString(R.string.grand_total)
        binding.grandTotalTxt.visibility = View.VISIBLE
    }

}