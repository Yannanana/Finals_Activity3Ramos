package com.example.finals_activity3ramos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finals_activity3ramos.R
import com.example.finals_activity3ramos.models.ProductAdmin

class ProductAdminAdapter(
    private var products: MutableList<ProductAdmin>,
    private val onEdit: (ProductAdmin) -> Unit,
    private val onDelete: (ProductAdmin) -> Unit
) : RecyclerView.Adapter<ProductAdminAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDescription)
        val imgEdit: ImageButton = itemView.findViewById(R.id.btnEditProduct)
        val imgDelete: ImageButton = itemView.findViewById(R.id.btnDeleteProduct)
        val ivImage: ImageView = itemView.findViewById(R.id.ivProductImage) // optional if you display image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_admin, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.tvName.text = product.name
        holder.tvDesc.text = "${product.description} • ₱${product.price} • Stock: ${product.stock}"


        // Optional: set image if using URL or drawable
        // holder.ivImage.setImage...

        holder.imgEdit.setOnClickListener { onEdit(product) }
        holder.imgDelete.setOnClickListener { onDelete(product) }
    }

    override fun getItemCount(): Int = products.size

    // Call this whenever the product list changes
    fun updateList(newProducts: List<ProductAdmin>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }
}
