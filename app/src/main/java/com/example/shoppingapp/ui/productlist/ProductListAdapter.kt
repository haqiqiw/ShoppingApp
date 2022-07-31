package com.example.shoppingapp.ui.productlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.R
import com.example.shoppingapp.core.util.capitalizeWords
import com.example.shoppingapp.databinding.ProductItemBinding
import com.example.shoppingapp.domain.model.Product

class ProductListAdapter(private val onClick: (Product) -> Unit) :
    ListAdapter<Product, ProductListAdapter.ProductViewHolder>(DiffCallback) {

    class ProductViewHolder(
        itemView: View,
        val onClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = ProductItemBinding.bind(itemView)
        private var currentProduct: Product? = null

        init {
            itemView.setOnClickListener {
                currentProduct?.let {
                    onClick(it)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) = with(binding) {
            currentProduct = product

            Glide.with(itemView)
                .load(product.image)
                .centerCrop()
                .into(ivThumbnail)

            tvName.text = product.title
            tvPrice.text = "$${product.price}"
            tvInfo.text = "★ ${product.rate} - ${product.category.capitalizeWords()}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    private object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
