package com.san.pizzaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.pizzaapp.databinding.ProductCrustItemRowBinding
import com.san.pizzaapp.model.CrustSize
import com.san.pizzaapp.utils.OnClickListener

class CrustSizeAdapter(
    var listener: OnClickListener
) : RecyclerView.Adapter<CrustSizeAdapter.MyViewHolder>() {

    private lateinit var list: ArrayList<CrustSize>
    private var defaultSizeData: Int = 0
    private var lastCheckedPosition = -1
    private var isChanged: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrustSizeAdapter.MyViewHolder {
        val binding = ProductCrustItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CrustSizeAdapter.MyViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.productCrustTxt.text = "${this.name} ${this.price}"

                if (isChanged) {
                    binding.radioBtnCrust.isChecked = (this.id.toInt() == defaultSizeData)
                } else {
                    binding.radioBtnCrust.isChecked = (position == lastCheckedPosition)
                }

                binding.productCrustTxt.setOnClickListener {
                    isChanged = false

                    if (lastCheckedPosition >= 0) {
                        notifyItemChanged(position)
                    }

                    listener.crustSizeRecord(this)

                    lastCheckedPosition = position
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setCrustList(crustSize: ArrayList<CrustSize>) {
        list = crustSize
    }

    fun setDefaultSize(defaultSize: Int) {
        defaultSizeData = defaultSize
    }

    fun setChanged(isChangedData: Boolean) {
        isChanged = isChangedData
    }

    inner class MyViewHolder(var binding: ProductCrustItemRowBinding) :
            RecyclerView.ViewHolder(binding.root)
}