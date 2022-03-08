package com.san.pizzaapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.san.pizzaapp.MainActivity
import com.san.pizzaapp.R
import com.san.pizzaapp.adapter.CrustAdapter
import com.san.pizzaapp.adapter.CrustSizeAdapter
import com.san.pizzaapp.databinding.FragmentCustomizeProductBottomSheetBinding
import com.san.pizzaapp.model.Crust
import com.san.pizzaapp.model.CrustSize
import com.san.pizzaapp.model.Product
import com.san.pizzaapp.model.ProductCart
import com.san.pizzaapp.room.CartDatabase
import com.san.pizzaapp.utils.OnClickListener
import com.san.pizzaapp.utils.Utils

class CustomizeProductBottomSheet(
    var product: Product,
    var mainActivity: MainActivity
) : BottomSheetDialogFragment(), OnClickListener {

    private lateinit var binding: FragmentCustomizeProductBottomSheetBinding
    private var cartCrust: Crust? = null
    private var cartCrustSize: CrustSize? = null
    private var cartProduct: Product? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomizeProductBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root

        val crusts: ArrayList<Crust> = product.crusts
        val crustAdapter = CrustAdapter(this)
        cartProduct = product

        crustAdapter.setCrustList(crusts)
        crustAdapter.setChanged(true)
        crustAdapter.setDefaultCrust(product.defaultCrust)

        binding.curstRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.curstRecyclerView.adapter = crustAdapter

        val db = Room.databaseBuilder(
            requireContext(),
            CartDatabase::class.java, Utils().DB_NAME
        ).allowMainThreadQueries().build()

        val cartDao = db.cartDao()
        val cartList: List<ProductCart> = cartDao.listAllCart()

        binding.addToCartBtn.setOnClickListener {

            if (cartCrust != null && cartCrustSize != null) {
                val crustExist: Int = cartDao.selectByCrustID(cartCrust!!.id).size
                val crustSizeExist: Int = cartDao.selectByCrustSizeID(cartCrustSize!!.id).size

                when {
                    crustSizeExist >= 1 && crustExist >= 1 -> {
                        Toast.makeText(context, "crust exists, goto cart to update cart", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        cartDao.addToCart(
                            ProductCart(
                                productName = product.name,
                                productCrustID = cartCrust!!.id,
                                productCrustName = cartCrust!!.name,
                                productCrustSizeID = cartCrustSize!!.id,
                                productCrustSizeName = cartCrustSize!!.name,
                                productPrice = cartCrustSize!!.price,
                                productCartCount = getString(R.string.one)
                            )
                        )

                        mainActivity.setBadge()

                        Toast.makeText(context, "Pizza added to Cart", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }
            } else {
                Toast.makeText(context, "Choose Crust and Crust Size", Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }

    override fun crustRecord(crust: Crust) {
        cartCrust = crust
        cartCrustSize = crust.sizes.filter {
            it.id.toInt() == crust.defaultSize
        }[0] // set default size on crust selected, before choosing crust size by manually

        setCrustSizes(crust.sizes, crust)
    }

    override fun crustSizeRecord(crustSize: CrustSize) {
        cartCrustSize = crustSize
    }

    private fun setCrustSizes(sizes: ArrayList<CrustSize>, crust: Crust) {
        val crustSizeAdapter = CrustSizeAdapter(this)

        // pass crust size for crust size adapter
        crustSizeAdapter.setCrustList(sizes)
        crustSizeAdapter.setDefaultSize(crust.defaultSize)
        crustSizeAdapter.setChanged(true)

        binding.curstSizeRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.curstSizeRecyclerView.adapter = crustSizeAdapter
    }

}