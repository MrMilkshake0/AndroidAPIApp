// app/src/main/java/com/example/nit3213/ui/screens/AssetAdapter.kt
package com.example.nit3213.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nit3213.R
import com.example.nit3213.data.AssetEntity
import java.text.NumberFormat
import java.util.Locale

class AssetAdapter(
    private val onClick: (AssetEntity) -> Unit
) : ListAdapter<AssetEntity, AssetAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<AssetEntity>() {
            override fun areItemsTheSame(oldItem: AssetEntity, newItem: AssetEntity): Boolean {
                // Prefer a stable id if you have one; fallback to equals
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: AssetEntity, newItem: AssetEntity): Boolean {
                return oldItem == newItem
            }
        }

        private fun currency(v: Double?): String =
            v?.let { NumberFormat.getCurrencyInstance(Locale.getDefault()).format(it) } ?: "—"

        /** If ≤1, treat as fraction (e.g., 0.65 -> 0.65%); else as whole percent (65 -> 65%). */
        private fun percent(v: Double?): String {
            if (v == null) return "—"
            val p = if (v <= 1.0) v * 100.0 else v
            return String.format(Locale.getDefault(), "%.2f%%", p)
        }
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleText)
        val subtitle: TextView = view.findViewById(R.id.subtitleText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_asset, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val e = getItem(position)

        // Summary must EXCLUDE description per spec.
        holder.title.text = e.ticker  // main label
        val type = e.assetType
        val price = currency(e.currentPrice)
        val yieldStr = percent(e.dividendYield)
        holder.subtitle.text = "Type: $type • Price: $price • Yield: $yieldStr"

        holder.itemView.setOnClickListener { onClick(e) }
    }
}
