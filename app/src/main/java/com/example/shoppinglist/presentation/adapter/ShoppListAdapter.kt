package com.example.shoppinglist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.databinding.ItemShopDisabledBinding
import com.example.shoppinglist.databinding.ItemShopEnabledBinding
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.presentation.callback.ShopListDiffCallback

class ShoppListAdapter : RecyclerView.Adapter<ShopItemViewHolder>() {

    var shopLists = listOf<ShopItem>()
        set(value) {
            val callback = ShopListDiffCallback(shopLists, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = when (viewType) {
            VIEW_TYPE_ENABLED -> ItemShopEnabledBinding.inflate(view, parent, false)
            VIEW_TYPE_DISABLED -> ItemShopDisabledBinding.inflate(view, parent, false)
            else -> throw RuntimeException("$viewType")
        }
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopLists[position]
        if (holder.binding is ItemShopEnabledBinding) {
            with(holder.binding) {
                tvName.text = shopItem.name
                tvCount.text = shopItem.count.toString()
            }
        }
        if (holder.binding is ItemShopDisabledBinding) {
            with(holder.binding) {
                tvName.text = shopItem.name
                tvCount.text = shopItem.count.toString()
            }
        }
        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    override fun getItemCount(): Int = shopLists.size

    override fun getItemViewType(position: Int): Int {
        val item = shopLists[position]
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 0
        const val VIEW_TYPE_DISABLED = 1
        const val MAX_POOL_SIZE = 15
    }
}