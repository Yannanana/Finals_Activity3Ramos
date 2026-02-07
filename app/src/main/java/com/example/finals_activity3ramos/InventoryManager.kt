package com.example.finals_activity3ramos

object InventoryManager {

    val categories = mutableMapOf<String, Category>()

    init {
        // Default category
        addCategory("Default")

        // Sample product
        addProduct(
            Product(
                id = "ARCH_FILE_A4",
                name = "ARCH FILE FOLDER A4, 2 RINGS (3\")",
                stock = 5,
                initialStock = 5,
                price = 85.0,
                category = "Default"
            )
        )
    }

    fun generateProductId(): String = "PROD_${System.currentTimeMillis()}"

    // CATEGORY MANAGEMENT
    fun addCategory(name: String) {
        if (!categories.containsKey(name)) {
            categories[name] = Category(name)
        }
    }

    fun editCategory(oldName: String, newName: String): Boolean {
        if (!categories.containsKey(oldName) || categories.containsKey(newName)) return false
        val category = categories.remove(oldName)!!
        category.name = newName
        category.products.forEach { it.category = newName }
        categories[newName] = category
        return true
    }

    fun removeCategory(name: String) {
        categories.remove(name)
    }

    fun getAllCategories(): List<Category> = categories.values.toList()

    fun getCategoryByName(name: String): Category? = categories[name]

    // PRODUCT MANAGEMENT
    fun addProduct(product: Product) {
        addCategory(product.category)
        categories[product.category]?.products?.add(product)
    }

    fun addProductToCategory(categoryName: String, product: Product) {
        product.category = categoryName
        addProduct(product)
    }

    fun getProductsByCategory(categoryName: String): List<Product> =
        categories[categoryName]?.products?.toList() ?: listOf()

    fun editProduct(updated: Product) {
        val category = categories[updated.category]
        category?.let {
            val index = it.products.indexOfFirst { p -> p.id == updated.id }
            if (index != -1) it.products[index] = updated else it.products.add(updated)
        }
    }

    fun removeProduct(productId: String) {
        categories.values.forEach { cat -> cat.products.removeIf { it.id == productId } }
    }
}
