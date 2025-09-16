package com.example.nit3213.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nit3213.data.AssetEntity
import com.example.nit3213.databinding.ActivityDetailsBinding
import com.google.gson.Gson
import java.text.NumberFormat
import androidx.core.view.WindowCompat

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ENTITY_JSON = "extra_entity_json"
    }

    private lateinit var binding: ActivityDetailsBinding
    private val gson = Gson()
    private val currency = NumberFormat.getCurrencyInstance()
    private val percent = NumberFormat.getPercentInstance().apply { minimumFractionDigits = 2 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle back button press on the toolbar
        binding.topAppBar.setNavigationOnClickListener {
            finish() // closes DetailsActivity and returns to DashboardActivity
        }

        val json = intent.getStringExtra(EXTRA_ENTITY_JSON)
        val entity = gson.fromJson(json, AssetEntity::class.java)

        binding.tvTitle.text = "${entity.ticker} • ${entity.assetType}"
        binding.tvPrice.text = "Price: " + (entity.currentPrice?.let { currency.format(it) } ?: "—")
        binding.tvYield.text = "Yield: " + (entity.dividendYield?.let { percent.format(it) } ?: "—")
        binding.tvDesc.text = entity.description ?: "—"
    }


}
