package com.san.pizzaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.pizzaapp.databinding.ProductCrustItemRowBinding
import com.san.pizzaapp.model.Crust
import com.san.pizzaapp.utils.OnClickListener

class CrustAdapter(
    private var listener: OnClickListener
) : RecyclerView.Adapter<CrustAdapter.MyViewHolder>() {

    private lateinit var list: ArrayList<Crust>
    private var lastCheckedPosition = -1
    private var defaultSizeData = 0
    private var isChanged: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrustAdapter.MyViewHolder {
        val binding = ProductCrustItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CrustAdapter.MyViewHolder, position: Int) {

        val pos = holder.adapterPosition

        with(holder) {

            with(list[pos]) {
                binding.productCrustTxt.text = this.name

                if (isChanged) {
                    binding.radioBtnCrust.isChecked = (this.id.toInt() == defaultSizeData)
                } else {
                    binding.radioBtnCrust.isChecked = (pos == lastCheckedPosition)
                }

                itemView.setOnClickListener {
                    isChanged = false

                    if (lastCheckedPosition >= 0) {
                        notifyDataSetChanged()
                    }

                    listener.crustRecord(this)

                    lastCheckedPosition = pos
                    notifyDataSetChanged()
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

    fun setChanged(isChangedData: Boolean) {
        isChanged = isChangedData
    }

    fun setDefaultCrust(defaultCrust: Int) {
        defaultSizeData = defaultCrust
    }

    inner class MyViewHolder(var binding: ProductCrustItemRowBinding) :
            RecyclerView.ViewHolder(binding.root)
}