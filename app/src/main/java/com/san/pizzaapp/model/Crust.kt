package com.san.pizzaapp.model

data class Crust(
    var id: String,
    var name: String,
    var defaultSize: Int,
    var sizes: ArrayList<CrustSize>
)