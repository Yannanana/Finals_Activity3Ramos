package com.example.finals_activity3ramos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val products: List<Product>,
    private val onCartUpdated: () -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.IV_ProductImage)
        val name: TextView = view.findViewById(R.id.TV_ProductName)
        val price: TextView = view.findViewById(R.id.TV_Price)
        val stock: TextView = view.findViewById(R.id.TV_Stock)
        val qty: TextView = view.findViewById(R.id.TV_quantity)
        val plus: ImageView = view.findViewById(R.id.BTN_plus)
        val minus: ImageView = view.findViewById(R.id.BTN_minus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        // Set product info
        holder.name.text = product.name
        holder.price.text = "₱${product.price}"
        holder.qty.text = CartManager.getItemQuantity(product.id).toString()

        // Set stock text and color
        if (product.stock > 0) {
            holder.stock.text = "In stock (${product.stock})"
            holder.stock.setTextColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.holo_green_dark)
            )
        } else {
            holder.stock.text = "Out of stock"
            holder.stock.setTextColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.holo_red_dark)
            )
        }


        // Set product image (example: static for now)
        holder.image.setImageResource(R.drawable.archfilefolder) // change dynamically later if needed

        // Plus button
        holder.plus.setOnClickListener {
            if (InventoryManager.decreaseStock(product.id)) {
                CartManager.addItem(product.id, product.name, product.price)
                notifyItemChanged(position)
                onCartUpdated()
            }
        }

        // Minus button
        holder.minus.setOnClickListener {
            if (CartManager.getItemQuantity(product.id) > 0) {
                CartManager.removeItem(product.id)
                InventoryManager.increaseStock(product.id)
                notifyItemChanged(position)
                onCartUpdated()
            }
        }
    }

    override fun getItemCount() = products.size
}
