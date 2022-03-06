package com.san.pizzaapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.san.pizzaapp.databinding.ProductCrustItemRowBinding
import com.san.pizzaapp.model.Crust
import com.san.pizzaapp.utils.OnClickListener

class CrustAdapter(
    private var listener: OnClickListener
) : RecyclerView.Adapter<CrustAdapter.MyViewHolder>() {

    private lateinit var list: ArrayList<Crust>
    private var lastCheckedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrustAdapter.MyViewHolder {
        val binding = ProductCrustItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CrustAdapter.MyViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.productCrustTxt.text = this.name

                binding.radioBtnCrust.isChecked = (position == lastCheckedPosition)

                binding.productCrustTxt.setOnClickListener {
                    if (lastCheckedPosition >= 0) {
                        notifyItemChanged(position)
                    }

                    listener.crustRecord(this)

                    lastCheckedPosition = position
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setCrustList(crusts: ArrayList<Crust>) {
        list = crusts
    }

    fun setListener() {

    }

    inner class MyViewHolder(var binding: ProductCrustItemRowBinding) :
            RecyclerView.ViewHolder(binding.root)
}