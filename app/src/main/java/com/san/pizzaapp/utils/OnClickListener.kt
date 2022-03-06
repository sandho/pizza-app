package com.san.pizzaapp.utils

import com.san.pizzaapp.model.Crust
import com.san.pizzaapp.model.CrustSize

interface OnClickListener {

    fun crustRecord(crust: Crust)
    fun crustSizeRecord(crustSize: CrustSize)

}