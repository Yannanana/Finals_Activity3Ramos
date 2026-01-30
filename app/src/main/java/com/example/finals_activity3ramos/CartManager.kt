package com.example.finals_activity3ramos

object CartManager {
    private val cartItems = mutableMapOf<String, CartItem>()

    fun addItem(item: CartItem) {
        val existing = cartItems[item.productId]
        if (existing != null) {
            existing.quantity++
        } else {
            cartItems[item.productId] = item.copy(quantity = 1)
        }
    }

    fun removeItem(productId: String) {
        val existing = cartItems[productId]
        if (existing != null) {
            existing.quantity--
            if (existing.quantity <= 0) {
                cartItems.remove(productId)
            }
        }
    }

    fun getTotalQuantity(): Int {
        return cartItems.values.sumOf { it.quantity }
    }

    fun getItems(): List<CartItem> {
        return cartItems.values.toList()
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun getItemQuantity(productId: String): Int {
        return cartItems[productId]?.quantity ?: 0
    }

}