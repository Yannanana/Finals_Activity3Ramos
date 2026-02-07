data class Product(
    val id: String,
    var name: String,
    var stock: Int,
    var initialStock: Int,
    var price: Double,
    var category: String,
    var imageUri: String? = null
)