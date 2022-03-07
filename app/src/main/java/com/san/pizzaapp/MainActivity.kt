package com.san.pizzaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.san.pizzaapp.databinding.ActivityMainBinding
import com.san.pizzaapp.room.CartDao
import com.san.pizzaapp.room.CartDatabase
import com.san.pizzaapp.ui.CartFragment
import com.san.pizzaapp.ui.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var cartDao: CartDao
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(HomeFragment()) // default fragment

        val db = Room.databaseBuilder(
            this,
            CartDatabase::class.java, "cart"
        ).allowMainThreadQueries().build()

        cartDao = db.cartDao()

        setBadge()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeMenu -> {
                    setFragment(HomeFragment())
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