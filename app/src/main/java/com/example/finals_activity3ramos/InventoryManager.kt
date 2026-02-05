import com.example.finals_activity3ramos.Product

object InventoryManager {

    private val products = mutableMapOf<String, Product>()

    init {
        // ORIGINAL PRODUCT (kept)
        addProduct(
            Product(
                id = "ARCH_FILE_A4",
                name = "ARCH FILE FOLDER A4, 2 RINGS (3\")",
                stock = 5,
                initialStock = 5,
                price = 85.0
            )
        )

        // DUMMY PRODUCTS (13 MORE)
        addProduct(Product("ARCH_FILE_LEGAL", "ARCH FILE FOLDER LEGAL", 5, 5, 95.0))
        addProduct(Product("EXP_FOLDER_A4", "EXPANDING FOLDER A4", 10, 10, 120.0))
        addProduct(Product("CLEAR_BOOK_40", "CLEAR BOOK 40 POCKETS", 8, 8, 150.0))
        addProduct(Product("CLEAR_BOOK_60", "CLEAR BOOK 60 POCKETS", 6, 6, 180.0))
        addProduct(Product("BOX_FILE", "BOX FILE WITH LOCK", 4, 4, 210.0))
        addProduct(Product("DOCUMENT_CASE", "DOCUMENT CASE A4", 12, 12, 75.0))
        addProduct(Product("PLASTIC_ENVELOPE", "PLASTIC ENVELOPE A4", 50, 50, 20.0))
        addProduct(Product("BROWN_ENVELOPE", "BROWN ENVELOPE A4", 50, 50, 15.0))
        addProduct(Product("INDEX_DIVIDER", "INDEX DIVIDER A4", 20, 20, 40.0))
        addProduct(Product("FOLDER_WITH_CLIP", "FOLDER WITH CLIP A4", 15, 15, 55.0))
        addProduct(Product("HANGING_FOLDER", "HANGING FOLDER", 10, 10, 90.0))
        addProduct(Product("LEVER_ARCH_FILE", "LEVER ARCH FILE", 7, 7, 160.0))
        addProduct(Product("FILE_TRAY", "FILE TRAY", 3, 3, 250.0))
    }

    fun addProduct(product: Product) {
        products[product.id] = product
    }

    fun getProduct(productId: String): Product? {
        return products[productId]
    }
    fun getAllProducts(): List<Product> {
        return products.values.toList()
    }


    fun decreaseStock(productId: String): Boolean {
        val product = products[productId] ?: return false

        if (product.stock > 0) {
            product.stock--
            return true
        }
        return false
    }

    fun increaseStock(productId: String) {
        val product = products[productId] ?: return
        if (product.stock < product.initialStock) {
            product.stock++
        }
    }
}
