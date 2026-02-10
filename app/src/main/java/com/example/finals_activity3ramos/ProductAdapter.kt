package com.example.finals_activity3ramos

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class ProductAdapter(
    private val context: Context,
    private var productList: MutableList<Product>,
    private val userId: Int,
    private val onCartUpdated: () -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val dbHelper = DatabaseHelper(context)

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
        val product = productList[position]

        holder.name.text = product.name
        holder.price.text = "₱${product.price}"
        holder.stock.text = if (product.stock > 0) "In stock (${product.stock})" else "Out of stock"
        holder.stock.setTextColor(
            ContextCompat.getColor(
                context,
                if (product.stock > 0) android.R.color.holo_green_dark else android.R.color.holo_red_dark
            )
        )

        if (!product.imagePath.isNullOrEmpty()) {
            Glide.with(context)
                .load(Uri.parse(product.imagePath))
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_placeholder)
        }



        // Get cart quantity from DB
        holder.qty.text = dbHelper.getCartQuantity(userId, product.id.toInt()).toString()

        // Plus button: add to cart and decrease stock in DB
        holder.plus.setOnClickListener {
            if (product.stock > 0) {
                product.stock--
                dbHelper.updateStock(product.id.toInt(), product.stock)
                dbHelper.addToCart(userId, product.id.toInt(), 1)
                holder.qty.text = dbHelper.getCartQuantity(userId, product.id.toInt()).toString()
                notifyItemChanged(position)
                onCartUpdated()
            }
        }

        // Minus button: remove from cart and increase stock in DB
        holder.minus.setOnClickListener {
            val qtyInCart = dbHelper.getCartQuantity(userId, product.id.toInt())
            if (qtyInCart > 0) {
                product.stock++
                dbHelper.updateStock(product.id.toInt(), product.stock)
                dbHelper.removeFromCart(userId, product.id.toInt(), 1)
                holder.qty.text = dbHelper.getCartQuantity(userId, product.id.toInt()).toString()
                notifyItemChanged(position)
                onCartUpdated()
            }
        }
    }



    fun updateList(newList: List<Product>) {
        productList.clear()
        productList.addAll(newList)
        notifyDataSetChanged()
    }
    override fun getItemCount() = productList.size

}
