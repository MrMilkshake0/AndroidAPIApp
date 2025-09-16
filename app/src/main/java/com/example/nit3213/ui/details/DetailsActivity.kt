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
        // Key used to pass the clicked item from Dashboard → Details
        const val EXTRA_ENTITY_JSON = "extra_entity_json"
    }

    private lateinit var binding: ActivityDetailsBinding
    private val gson = Gson()

    // For nice formatting of numbers
    private val currency = NumberFormat.getCurrencyInstance()
    private val percent = NumberFormat.getPercentInstance().apply { minimumFractionDigits = 2 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar back button → return to Dashboard
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        // Get the JSON for this entity from the Intent
        val json = intent.getStringExtra(EXTRA_ENTITY_JSON)
        val entity = gson.fromJson(json, AssetEntity::class.java)

        // Fill in the UI with the entity’s data
        binding.tvTitle.text = "${entity.ticker} • ${entity.assetType}"
        binding.tvPrice.text = "Price: " + (entity.currentPrice?.let { currency.format(it) } ?: "—")
        binding.tvYield.text = "Yield: " + (entity.dividendYield?.let { percent.format(it) } ?: "—")
        binding.tvDesc.text = entity.description ?: "—"
    }
}

