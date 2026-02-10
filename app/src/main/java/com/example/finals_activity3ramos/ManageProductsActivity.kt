package com.example.finals_activity3ramos

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finals_activity3ramos.adapters.ProductAdminAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.finals_activity3ramos.models.Category
import com.example.finals_activity3ramos.models.ProductAdmin




class ManageProductsActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var spinnerCategories: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddProduct: FloatingActionButton
    private var categoryList = mutableListOf<Category>()
    private var selectedCategoryId: Int = -1
    private var selectedImageUri: Uri? = null
    private lateinit var adminAdapter: ProductAdminAdapter
    private var productList = mutableListOf<ProductAdmin>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manage_products)

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val back = findViewById<ImageView>(R.id.BTN_Back)
        back.setOnClickListener {
            val intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)
        }

        fabAddProduct = findViewById(R.id.fabAddProduct)
        fabAddProduct.setOnClickListener {
            showAddProductDialog()
        }

        db = DatabaseHelper(this)
        spinnerCategories = findViewById(R.id.spinnerCategories)
        recyclerView = findViewById(R.id.RV_ManageProducts)

        setupSpinner()
        setupRecyclerView()

    }

    private fun setupSpinner() {
        categoryList.clear()
        val cursor = db.getAllCategories()
        if (cursor.moveToFirst()) {
            do {
                categoryList.add(
                    Category(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("category_id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("category_name"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categoryList.map { it.name }
        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories.adapter = spinnerAdapter

        if (categoryList.isNotEmpty()) {
            spinnerCategories.setSelection(0)
        }

        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedCategoryId = categoryList[position].id
                loadProducts(selectedCategoryId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)

        adminAdapter = ProductAdminAdapter(
            productList,
            onEdit = { product ->
                showEditProductDialog(product) // or TODO()
            },
            onDelete = { product ->
                db.deleteProduct(product.id)
                loadProducts(selectedCategoryId)
            }
        )

        recyclerView.adapter = adminAdapter
    }


    private fun loadProducts(categoryId: Int) {
        productList.clear()
        productList.addAll(db.getAdminProductsByCategory(categoryId))
        adminAdapter.notifyDataSetChanged()
    }
    private fun showEditProductDialog(product: ProductAdmin) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_product, null)

        val etName = dialogView.findViewById<EditText>(R.id.etProductName)
        val etDesc = dialogView.findViewById<EditText>(R.id.etDescription)
        val etPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val etStock = dialogView.findViewById<EditText>(R.id.etStock)
        val ivImage = dialogView.findViewById<ImageView>(R.id.ivProductImage)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        // 🟢 PRE-FILL EXISTING DATA
        etName.setText(product.name)
        etDesc.setText(product.description)
        etPrice.setText(product.price.toString())
        etStock.setText(product.stock.toString())

        // Optional: keep old image
        var imageUri: String? = product.imagePath

        ivImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 2001)
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val desc = etDesc.text.toString().trim()
            val price = etPrice.text.toString().toDoubleOrNull()
            val stock = etStock.text.toString().toIntOrNull()

            if (name.isEmpty() || desc.isEmpty() || price == null || stock == null) {
                Toast.makeText(this, "Fill all fields correctly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = db.updateProduct(
                product.id,
                name,
                desc,
                price,
                stock,
                imageUri
            )

            if (success) {
                Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show()
                loadProducts(selectedCategoryId)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }


    private fun showAddProductDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_product, null)
        val etName = dialogView.findViewById<EditText>(R.id.etProductName)
        val etDesc = dialogView.findViewById<EditText>(R.id.etDescription)
        val etPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val etStock = dialogView.findViewById<EditText>(R.id.etStock)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val ivImage = dialogView.findViewById<ImageView>(R.id.ivProductImage)

        ivImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1001)
        }
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val desc = etDesc.text.toString().trim()
            val price = etPrice.text.toString().toDoubleOrNull()
            val stock = etStock.text.toString().toIntOrNull()

            if (name.isEmpty() || desc.isEmpty() || price == null || stock == null) {
                Toast.makeText(this, "All fields must be filled correctly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedCategoryId == -1) {
                Toast.makeText(this, "Select a category first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (db.addProduct(selectedCategoryId, name, desc, price, stock, selectedImageUri?.toString())) {
                Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show()
                loadProducts(selectedCategoryId)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener { dialog.dismiss() }
    }
    override fun onResume() {
        super.onResume()
        setupSpinner()
        if (selectedCategoryId != -1) {
            loadProducts(selectedCategoryId)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
        }
    }



}
