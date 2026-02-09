package com.example.finals_activity3ramos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.RecyclerView
import com.example.finals_activity3ramos.R
import com.example.finals_activity3ramos.models.Category

class CategoryHomeAdapter(
    private var categories: MutableList<Category>,
    private val onClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryHomeAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnCategory: MaterialButton = itemView.findViewById(R.id.btnCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_home, parent, false)
        return CategoryViewHolder(view)
    }


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.btnCategory.text = category.name
        holder.btnCategory.setOnClickListener {
            onClick(category)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateList(newList: List<Category>) {
        categories.clear()
        categories.addAll(newList)
        notifyDataSetChanged()
    }
}
