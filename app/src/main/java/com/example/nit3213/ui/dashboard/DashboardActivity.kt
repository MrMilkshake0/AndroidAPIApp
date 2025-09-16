package com.example.nit3213.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nit3213.data.AssetEntity
import com.example.nit3213.databinding.ActivityDashboardBinding
import com.example.nit3213.ui.details.DetailsActivity
import com.example.nit3213.util.UiState
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.core.view.WindowCompat
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val vm: DashboardViewModel by viewModels()
    private val gson = Gson()
    private val adapter = AssetAdapter { entity ->
        val json = gson.toJson(entity)
        startActivity(Intent(this, DetailsActivity::class.java)
            .putExtra(DetailsActivity.EXTRA_ENTITY_JSON, json))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvAssets.layoutManager = LinearLayoutManager(this)
        binding.rvAssets.adapter = adapter

        lifecycleScope.launch {
            vm.state.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        binding.progress.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        adapter.submitList(state.data)
                    }
                    is UiState.Error -> {
                        binding.progress.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = state.message
                        adapter.submitList(emptyList())
                    }
                    is UiState.Idle -> Unit
                }
            }
        }

        // initial load
        vm.load()
    }
}
