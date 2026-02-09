package com.example.finals_activity3ramos.models

data class ProductAdmin(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val imagePath: String?
)
