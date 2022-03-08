package com.san.pizzaapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.room.Room
import com.google.gson.Gson
import com.san.pizzaapp.MainActivity
import com.san.pizzaapp.R
import com.san.pizzaapp.databinding.FragmentHomeBinding
import com.san.pizzaapp.model.Product
import com.san.pizzaapp.network.Resource
import com.san.pizzaapp.room.CartDao
import com.san.pizzaapp.room.CartDatabase
import com.san.pizzaapp.utils.Utils
import com.san.pizzaapp.utils.getAssetsJSON
import com.san.pizzaapp.utils.setPriceWithRupeesSymbol
import com.san.pizzaapp.viewModel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment(var mainActivity: MainActivity) : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val mainViewModel: MainViewModel by viewModel()

    companion object {
        private const val TAG = "HomeFragment"
        lateinit var productAsString: String
        var product: Product? = null
        lateinit var products: ArrayList<Product>
        lateinit var cartDao: CartDao
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        if (Utils().NETWORK_CALL) {
            mainViewModel.pizzaList()

            mainViewModel.pizzaList.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Resource.Failure -> {
                        binding.progressBar.visibility = View.GONE
                        binding.productLayout.visibility = View.GONE
                        binding.emptyProductTxt.visibility = View.VISIBLE
                        Toast.makeText(context, "Load Failed...", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.productLayout.visibility = View.VISIBLE
                        binding.emptyProductTxt.visibility = View.GONE

                        product = it.value
                        setProductIntoView(product)
                    }
                }
            })
        } else {
            binding.productLayout.visibility = View.VISIBLE
            binding.emptyProductTxt.visibility = View.GONE

            productAsString = requireActivity().getAssetsJSON("pizzas.json") // get json data, from local assets
            product = Gson().fromJson(productAsString, Product::class.java) // converting string into json using Gson
            setProductIntoView(product)
        }

        val db = Room.databaseBuilder(
            requireContext(),
            CartDatabase::class.java, Utils().DB_NAME
        ).allowMainThreadQueries().build()

        cartDao = db.cartDao()

        return binding.root
    }

    private fun setProductIntoView(product: Product?) {
        binding.productNameTxt.text = product!!.name
        binding.productDescTxt.text = product.description

        if (product.isVeg) {
            binding.productIsVeg.setImageResource(R.drawable.ic_veg_icon)
        }

        val bottomSheet = CustomizeProductBottomSheet(product, mainActivity)

        // opening bottom sheet for add custom product to cart
        binding.addBtn.setOnClickListener {
            bottomSheet.show(
                requireActivity().supportFragmentManager,
                bottomSheet.tag
            )
        }

        val leastValue = product.crusts.minOf {
            it.sizes.minOf {
                it.price
            }
        } // get least price from the crust sizes

        binding.productPriceTxt.text = leastValue.setPriceWithRupeesSymbol() // prefix with rupees symbol
    }

}