package com.example.finals_activity3ramos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finals_activity3ramos.R
import com.example.finals_activity3ramos.models.Category


class CategoryAdapter(
    private var categories: MutableList<Category>,
    private val onEdit: (Category) -> Unit,
    private val onDelete: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEditCategory)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_admin, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.tvName.text = category.name

        holder.btnEdit.setOnClickListener { onEdit(category) }
        holder.btnDelete.setOnClickListener { onDelete(category) }
    }

    override fun getItemCount(): Int = categories.size

    // Refresh list when data changes
    fun updateList(newCategories: List<Category>) {
        categories.clear()
        categories.addAll(newCategories)
        notifyDataSetChanged()
    }
}
