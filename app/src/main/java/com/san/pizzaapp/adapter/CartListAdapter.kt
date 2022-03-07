package com.san.pizzaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.pizzaapp.databinding.CartListRowBinding
import com.san.pizzaapp.databinding.ProductCrustItemRowBinding
import com.san.pizzaapp.model.ProductCart
import com.san.pizzaapp.ui.CartFragment
import com.san.pizzaapp.utils.CartUpdateListener
import com.san.pizzaapp.utils.OnClickListener
import com.san.pizzaapp.utils.setPriceWithRupeesSymbol

class CartListAdapter(
    var listener: CartUpdateListener
) : RecyclerView.Adapter<CartListAdapter.MyViewHolder>() {

    private lateinit var list: List<ProductCart>

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartListAdapter.MyViewHolder {
        val binding = CartListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartListAdapter.MyViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {

                var num: Int = 0

                binding.productNameTxt.text = this.productName
                binding.productCrustNameTxt.text = this.productCrustName+" "+this.productCrustSizeName

                binding.deleteCartBtn.setOnClickListener {
                    listener.deleteCartItem(this.id)
                }

                num = this.productCartCount.toInt()
                binding.countTxt.text = this.productCartCount
                binding.productPriceTxt.text = this.productPrice.setPriceWithRupeesSymbol()
                binding.productSubTotalTxt.text = (this.productCartCount.toDouble() * this.productPrice.toDouble()).toString().setPriceWithRupeesSymbol()

                binding.incrementBtn.setOnClickListener {
                    if (num >= 0) {
                        num = num.inc()
                        binding.countTxt.text = (num.toString())
                    }

                    listener.updateCart(this, num.toString(), position)
                }

                binding.decrementBtn.setOnClickListener {
                    if (num > 0) {
                        num = num.dec()
                        binding.countTxt.text = (num.toString())
                    } else {
                        notifyItemRemoved(position)
                    }

                    listener.updateCart(this, num.toString(), position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setCrustList(cartList: List<ProductCart>) {
        list = cartList
    }

    inner class MyViewHolder(var binding: CartListRowBinding) :
        RecyclerView.ViewHolder(binding.root)

}