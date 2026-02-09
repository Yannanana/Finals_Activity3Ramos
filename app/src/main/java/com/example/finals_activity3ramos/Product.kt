package com.example.finals_activity3ramos

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    var stock: Int,
    val imagePath: String?
)

