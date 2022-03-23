package com.adit.bangkit.plagroid.ui.activity.seller.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.model.Product

class ListProductAdapter(private val listProduct: ArrayList<Product>) : RecyclerView.Adapter<ListProductAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_product, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val product: Product = listProduct[position]
        holder.productPict.setImageResource(product.productPict.toInt())
        holder.productName.setText(product.productName)
        holder.productPrice.setText(product.productPrice.toString())
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listProduct[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listProduct.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productPict: ImageView = itemView.findViewById(R.id.img_item_photo)
        var productName: TextView = itemView.findViewById(R.id.tv_item_name)
        var productPrice: TextView = itemView.findViewById(R.id.tv_item_harga)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Product)
    }
}