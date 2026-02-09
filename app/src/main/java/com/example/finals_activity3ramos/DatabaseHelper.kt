package com.example.finals_activity3ramos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "NU_Needs.db"
        private const val DATABASE_VERSION = 5

        // 1. Users Table
        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_FIRST_NAME = "first_name"
        private const val COLUMN_LAST_NAME = "last_name"
        private const val COLUMN_MIDDLE_NAME = "middle_name"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        // 2. Categories Table
        private const val TABLE_CATEGORIES = "categories"
        private const val COLUMN_CATEGORY_ID = "category_id"
        private const val COLUMN_CATEGORY_NAME = "category_name"

        // 3. Products Table
        private const val TABLE_PRODUCTS = "products"
        private const val COLUMN_PRODUCT_ID = "product_id"
        private const val COLUMN_PRODUCT_CAT_ID = "category_id"
        private const val COLUMN_PRODUCT_NAME = "name"
        private const val COLUMN_PRODUCT_DESC = "description"
        private const val COLUMN_PRODUCT_PRICE = "price"
        private const val COLUMN_PRODUCT_STOCK = "stock"
        private const val COLUMN_PRODUCT_IMAGE = "image_url"

        // 4. Cart Table
        private const val TABLE_CART = "cart"
        private const val COLUMN_CART_ID = "cart_id"
        private const val COLUMN_CART_USER_ID = "user_id"
        private const val COLUMN_CART_PROD_ID = "product_id"
        private const val COLUMN_CART_QTY = "quantity"

        // 5. Orders (Receipts) Table
        private const val TABLE_ORDERS = "orders"
        private const val COLUMN_ORDER_ID = "order_id"
        private const val COLUMN_ORDER_USER_ID = "user_id"
        private const val COLUMN_ORDER_DATE = "date"
        private const val COLUMN_ORDER_TOTAL = "total_amount"

        // 6. Order Items Table
        private const val TABLE_ORDER_ITEMS = "order_items"
        private const val COLUMN_OI_ID = "order_item_id"
        private const val COLUMN_OI_ORDER_ID = "order_id"
        private const val COLUMN_OI_PROD_ID = "product_id"
        private const val COLUMN_OI_QTY = "quantity"
        private const val COLUMN_OI_PRICE = "price"
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_USERS ($COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_FIRST_NAME TEXT, $COLUMN_LAST_NAME TEXT, $COLUMN_MIDDLE_NAME TEXT, $COLUMN_USERNAME TEXT UNIQUE, $COLUMN_PASSWORD TEXT)")
        db?.execSQL("CREATE TABLE $TABLE_CATEGORIES ($COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_CATEGORY_NAME TEXT)")
        db?.execSQL("CREATE TABLE $TABLE_PRODUCTS ($COLUMN_PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_PRODUCT_CAT_ID INTEGER, $COLUMN_PRODUCT_NAME TEXT, $COLUMN_PRODUCT_DESC TEXT, $COLUMN_PRODUCT_PRICE REAL, $COLUMN_PRODUCT_STOCK INTEGER, $COLUMN_PRODUCT_IMAGE TEXT, FOREIGN KEY($COLUMN_PRODUCT_CAT_ID) REFERENCES $TABLE_CATEGORIES($COLUMN_CATEGORY_ID))")
        db?.execSQL("CREATE TABLE $TABLE_CART ($COLUMN_CART_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_CART_USER_ID INTEGER, $COLUMN_CART_PROD_ID INTEGER, $COLUMN_CART_QTY INTEGER, FOREIGN KEY($COLUMN_CART_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID), FOREIGN KEY($COLUMN_CART_PROD_ID) REFERENCES $TABLE_PRODUCTS($COLUMN_PRODUCT_ID))")
        db?.execSQL("CREATE TABLE $TABLE_ORDERS ($COLUMN_ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_ORDER_USER_ID INTEGER, $COLUMN_ORDER_DATE TEXT, $COLUMN_ORDER_TOTAL REAL, FOREIGN KEY($COLUMN_ORDER_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID))")
        db?.execSQL("CREATE TABLE $TABLE_ORDER_ITEMS ($COLUMN_OI_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_OI_ORDER_ID INTEGER, $COLUMN_OI_PROD_ID INTEGER, $COLUMN_OI_QTY INTEGER, $COLUMN_OI_PRICE REAL, FOREIGN KEY($COLUMN_OI_ORDER_ID) REFERENCES $TABLE_ORDERS($COLUMN_ORDER_ID), FOREIGN KEY($COLUMN_OI_PROD_ID) REFERENCES $TABLE_PRODUCTS($COLUMN_PRODUCT_ID))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ORDER_ITEMS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // --- User Methods ---
    fun addUser(firstName: String, lastName: String, middleName: String, username: String, pass: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FIRST_NAME, firstName); put(COLUMN_LAST_NAME, lastName)
            put(COLUMN_MIDDLE_NAME, middleName); put(COLUMN_USERNAME, username); put(COLUMN_PASSWORD, pass)
        }
        return db.insert(TABLE_USERS, null, values) != -1L
    }

    fun checkUser(username: String, pass: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME=? AND $COLUMN_PASSWORD=?", arrayOf(username, pass))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // --- Category Methods ---
    fun addCategory(name: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply { put(COLUMN_CATEGORY_NAME, name) }
        return db.insert(TABLE_CATEGORIES, null, values) != -1L
    }

    // --- Product Methods ---
    fun addProduct(catId: Int, name: String, desc: String, price: Double, stock: Int, img: String?): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PRODUCT_CAT_ID, catId); put(COLUMN_PRODUCT_NAME, name)
            put(COLUMN_PRODUCT_DESC, desc); put(COLUMN_PRODUCT_PRICE, price)
            put(COLUMN_PRODUCT_STOCK, stock); put(COLUMN_PRODUCT_IMAGE, img)
        }
        return db.insert(TABLE_PRODUCTS, null, values) != -1L
    }


    // IMPORTANT: Call this to check if cart is empty before checkout
    fun getCartCount(userId: Int): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_CART WHERE $COLUMN_CART_USER_ID=?", arrayOf(userId.toString()))
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

    fun clearCart(userId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_CART, "$COLUMN_CART_USER_ID=?", arrayOf(userId.toString()))
    }
    fun getAllCategories(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM categories", null)
    }

    fun deleteCategory(id: Int): Boolean {
        val db = this.writableDatabase
        return db.delete(TABLE_CATEGORIES, "$COLUMN_CATEGORY_ID=?", arrayOf(id.toString())) > 0
    }

    fun updateCategory(id: Int, name: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY_NAME, name)
        }
        return db.update(TABLE_CATEGORIES, values, "$COLUMN_CATEGORY_ID=?", arrayOf(id.toString())) > 0
    }


    fun getProductsByCategory(categoryId: Int): MutableList<Product> {
        val products = mutableListOf<Product>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_PRODUCTS WHERE $COLUMN_PRODUCT_CAT_ID = ?",
            arrayOf(categoryId.toString())
        )

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_DESC))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_PRICE))
            val stock = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_STOCK))
            val imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_IMAGE))

            products.add(
                Product(
                    id = id,
                    name = name,
                    description = description,
                    price = price,
                    stock = stock,
                    imagePath = imagePath
                )
            )
        }

        cursor.close()
        return products
    }



    // Update stock for a product
    fun updateStock(productId: Int, newStock: Int) {
        writableDatabase.execSQL(
            "UPDATE $TABLE_PRODUCTS SET $COLUMN_PRODUCT_STOCK = ? WHERE $COLUMN_PRODUCT_ID = ?",
            arrayOf(newStock, productId)
        )
    }
    fun increaseProductStock(productId: String, quantity: Int) {
        val db = writableDatabase
        db.execSQL(
            "UPDATE $TABLE_PRODUCTS SET $COLUMN_PRODUCT_STOCK = $COLUMN_PRODUCT_STOCK + ? WHERE $COLUMN_PRODUCT_ID = ?",
            arrayOf(quantity.toString(), productId)
        )
    }


    // Get cart quantity for a product
    fun getCartQuantity(userId: Int, productId: Int): Int {
        val cursor = readableDatabase.rawQuery(
            "SELECT $COLUMN_CART_QTY FROM $TABLE_CART WHERE $COLUMN_CART_USER_ID = ? AND $COLUMN_CART_PROD_ID = ?",
            arrayOf(userId.toString(), productId.toString())
        )
        val qty = if (cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        return qty
    }

    // Add to cart (insert or update)
    fun addToCart(userId: Int, productId: Int, qty: Int) {
        val currentQty = getCartQuantity(userId, productId)
        val newQty = currentQty + qty
        val db = writableDatabase
        if (currentQty > 0) {
            val cv = ContentValues().apply { put(COLUMN_CART_QTY, newQty) }
            db.update(TABLE_CART, cv, "$COLUMN_CART_USER_ID=? AND $COLUMN_CART_PROD_ID=?", arrayOf(userId.toString(), productId.toString()))
        } else {
            val cv = ContentValues().apply {
                put(COLUMN_CART_USER_ID, userId)
                put(COLUMN_CART_PROD_ID, productId)
                put(COLUMN_CART_QTY, qty)
            }
            db.insert(TABLE_CART, null, cv)
        }
    }

    // Remove from cart
    fun removeFromCart(userId: Int, productId: Int, qty: Int) {
        val currentQty = getCartQuantity(userId, productId)
        val newQty = currentQty - qty
        val db = writableDatabase
        if (newQty > 0) {
            val cv = ContentValues().apply { put(COLUMN_CART_QTY, newQty) }
            db.update(TABLE_CART, cv, "$COLUMN_CART_USER_ID=? AND $COLUMN_CART_PROD_ID=?", arrayOf(userId.toString(), productId.toString()))
        } else {
            db.delete(TABLE_CART, "$COLUMN_CART_USER_ID=? AND $COLUMN_CART_PROD_ID=?", arrayOf(userId.toString(), productId.toString()))
        }
    }







}
