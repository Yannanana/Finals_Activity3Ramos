package com.example.finals_activity3ramos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finals_activity3ramos.R
import com.example.finals_activity3ramos.models.Category


class CategoryAdapter(
    private val categories: MutableList<Category>,
    private val onEdit: (Category) -> Unit,
    private val onDelete: (Category, Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val btnEdit: ImageView = itemView.findViewById(R.id.btnEditCategory)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDeleteCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_admin, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        holder.tvName.text = category.name

        holder.btnEdit.setOnClickListener {
            onEdit(category)
        }

        holder.btnDelete.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onDelete(category, pos)
            }
        }
    }

    override fun getItemCount(): Int = categories.size

    // ✅ SAFE REMOVE METHOD
    fun removeAt(position: Int) {
        categories.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, categories.size)
    }
}
