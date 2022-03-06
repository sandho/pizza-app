package com.san.pizzaapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.san.pizzaapp.model.ProductCart

@Database(entities = [ProductCart::class], version = 1)
abstract class CartDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao
}