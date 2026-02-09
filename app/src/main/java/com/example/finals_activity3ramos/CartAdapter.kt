package com.example.finals_activity3ramos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val items: MutableList<CartItem>,
    private val dbHelper: DatabaseHelper,
    private val onUpdate: () -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtItem: TextView = view.findViewById(R.id.TV_item)
        val btnDelete: ImageView = view.findViewById(R.id.BTN_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.txtItem.text =
            "${item.quantity}x ${item.name}, ₱${item.price}"

        holder.btnDelete.setOnClickListener {
            dbHelper.increaseProductStock(item.productId, item.quantity)
            repeat(item.quantity) {
                CartManager.removeItem(item.productId)

            }
            items.removeAt(position)
            notifyItemRemoved(position)
            onUpdate()
        }
    }

    override fun getItemCount() = items.size
}