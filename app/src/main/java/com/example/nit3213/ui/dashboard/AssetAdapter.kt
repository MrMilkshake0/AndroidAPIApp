package com.example.nit3213.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nit3213.data.AssetEntity
import com.example.nit3213.databinding.ItemAssetBinding
import java.text.NumberFormat

class AssetAdapter(
    private val onClick: (AssetEntity) -> Unit
) : ListAdapter<AssetEntity, AssetAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<AssetEntity>() {
            override fun areItemsTheSame(old: AssetEntity, new: AssetEntity) =
                old.ticker == new.ticker && old.assetType == new.assetType
            override fun areContentsTheSame(old: AssetEntity, new: AssetEntity) = old == new
        }
        private val currency = NumberFormat.getCurrencyInstance()
        private val percent = NumberFormat.getPercentInstance().apply { minimumFractionDigits = 2 }
    }

    inner class VH(val b: ItemAssetBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: AssetEntity) {
            b.tvTicker.text = item.ticker
            b.tvType.text = item.assetType
            b.tvPrice.text = "Price: " + (item.currentPrice?.let { currency.format(it) } ?: "—")
            b.tvYield.text = "Yield: " + (item.dividendYield?.let { percent.format(it) } ?: "—")
            b.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemAssetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}
