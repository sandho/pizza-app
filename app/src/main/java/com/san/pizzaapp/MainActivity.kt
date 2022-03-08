package com.san.pizzaapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.room.Room
import com.san.pizzaapp.databinding.ActivityMainBinding
import com.san.pizzaapp.model.ProductCart
import com.san.pizzaapp.room.CartDao
import com.san.pizzaapp.room.CartDatabase
import com.san.pizzaapp.ui.CartFragment
import com.san.pizzaapp.ui.HomeFragment
import com.san.pizzaapp.utils.Utils
import com.san.pizzaapp.viewModel.RoomDbViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val roomDbViewModel: RoomDbViewModel by viewModel()
    private var cartList: List<ProductCart> = ArrayList<ProductCart>()

    private lateinit var cartDao: CartDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(HomeFragment(this)) // default fragment

        /*roomDbViewModel.listAllCart()
        roomDbViewModel.listCart.observe(this, Observer {
            cartList = it
        })*/

        val db = Room.databaseBuilder(
            this,
            CartDatabase::class.java, Utils().DB_NAME
        ).allowMainThreadQueries().build()

        cartDao = db.cartDao()

        setBadge()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeMenu -> {
                    setFragment(HomeFragment(this))
                }
                R.id.cartMenu -> {
                    setFragment(CartFragment(this))
                }
            }

            true
        }
    }

    fun setBadge() {
        binding.bottomNavigationView.getOrCreateBadge(R.id.cartMenu).number = cartDao.listAllCart().sumOf {
            it.productCartCount.toInt()
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .apply {
                replace(R.id.frameLayout, fragment)
                commit()
            }
    }

}