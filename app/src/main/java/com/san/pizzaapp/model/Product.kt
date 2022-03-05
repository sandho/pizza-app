package com.san.pizzaapp.model

data class Product(
    var name: String,
    var isVeg: Boolean,
    var description: String,
    var defaultCrust: Int,
    var crusts: ArrayList<Crust>
)