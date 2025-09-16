package com.example.nit3213.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nit3213.data.AssetEntity
import com.example.nit3213.databinding.ItemAssetBinding
import java.text.NumberFormat

// Adapter is the "bridge" between raw data (AssetEntity) and the RecyclerView (list on screen).
// It knows how to create rows (ViewHolders) and bind each AssetEntity into a row.
class AssetAdapter(
    private val onClick: (AssetEntity) -> Unit // function to run when user taps a row
) : ListAdapter<AssetEntity, AssetAdapter.VH>(DIFF) {

    companion object {
        // DiffUtil helps RecyclerView know exactly what changed in the list
        // so it can animate smartly instead of redrawing everything.
        private val DIFF = object : DiffUtil.ItemCallback<AssetEntity>() {
            override fun areItemsTheSame(old: AssetEntity, new: AssetEntity) =
                old.ticker == new.ticker && old.assetType == new.assetType

            override fun areContentsTheSame(old: AssetEntity, new: AssetEntity) = old == new
        }

        // Formatters for pretty display of numbers
        private val currency = NumberFormat.getCurrencyInstance()
        private val percent = NumberFormat.getPercentInstance().apply { minimumFractionDigits = 2 }
    }

    // ViewHolder = one "row" in the list.
    // It holds a binding to the item_asset.xml layout and knows how to fill it in.
    inner class VH(val b: ItemAssetBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: AssetEntity) {
            // Fill in text fields with the AssetEntity’s data
            b.tvTicker.text = item.ticker
            b.tvType.text = item.assetType
            b.tvPrice.text = "Price: " + (item.currentPrice?.let { currency.format(it) } ?: "—")
            b.tvYield.text = "Yield: " + (item.dividendYield?.let { percent.format(it) } ?: "—")

            // Make the whole row clickable → passes the AssetEntity back to DashboardActivity
            b.root.setOnClickListener { onClick(item) }
        }
    }

    // Called when RecyclerView needs a new row view to display.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemAssetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    // Called when RecyclerView needs to put data into a row view.
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}

