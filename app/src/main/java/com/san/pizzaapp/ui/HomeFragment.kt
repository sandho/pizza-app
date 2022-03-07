package com.san.pizzaapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.gson.Gson
import com.san.pizzaapp.R
import com.san.pizzaapp.adapter.CartListAdapter
import com.san.pizzaapp.databinding.FragmentHomeBinding
import com.san.pizzaapp.model.Crust
import com.san.pizzaapp.model.CrustSize
import com.san.pizzaapp.model.Product
import com.san.pizzaapp.model.ProductCart
import com.san.pizzaapp.room.CartDao
import com.san.pizzaapp.room.CartDatabase
import com.san.pizzaapp.utils.OnClickListener
import com.san.pizzaapp.utils.Utils
import com.san.pizzaapp.utils.getAssetsJSON
import com.san.pizzaapp.utils.setPriceWithRupeesSymbol

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    companion object {
        private const val TAG = "HomeFragment"
        lateinit var productAsString: String
        lateinit var product: Product
        lateinit var products: ArrayList<Product>
        lateinit var cartDao: CartDao
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        productAsString = requireActivity().getAssetsJSON("pizzas.json") // get json data, from local assets
        product = Gson().fromJson(productAsString, Product::class.java) // converting string into json using Gson

        products = ArrayList() // converting product object into products arraylist for recyclerview
        products.add(product)

        setProductIntoView(product)

        val db = Room.databaseBuilder(
            requireContext(),
            CartDatabase::class.java, "cart"
        ).allowMainThreadQueries().build()

        cartDao = db.cartDao()

        val leastValue = product.crusts.minOf {
            it.sizes.minOf { 
                it.price
            }
        } // get least price from the crust sizes

        binding.productPriceTxt.text = leastValue.setPriceWithRupeesSymbol() // prefix with rupees symbol

        return binding.root
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
                requireActivity().supportFragmentManager,
                bottomSheet.tag
            )
        }
    }

}