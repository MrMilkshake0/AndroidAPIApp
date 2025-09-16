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

    // ViewModel controls loading of dashboard data
    private val vm: DashboardViewModel by viewModels()

    // Gson is used to convert objects into JSON strings for navigation
    private val gson = Gson()

    // Adapter displays our list of assets in the RecyclerView
    private val adapter = AssetAdapter { entity ->
        // On click: turn the entity into JSON and open the DetailsActivity
        val json = gson.toJson(entity)
        startActivity(
            Intent(this, DetailsActivity::class.java)
                .putExtra(DetailsActivity.EXTRA_ENTITY_JSON, json)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Fullscreen, no default system insets
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView with vertical list layout
        binding.rvAssets.layoutManager = LinearLayoutManager(this)
        binding.rvAssets.adapter = adapter

        // ---- Watch the ViewModel state ----
        lifecycleScope.launch {
            vm.state.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // Show loading spinner, hide error
                        binding.progress.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        // Hide spinner & error, show list
                        binding.progress.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        adapter.submitList(state.data) // load items into list
                    }
                    is UiState.Error -> {
                        // Show error message
                        binding.progress.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = state.message
                        adapter.submitList(emptyList()) // clear the list
                    }
                    is UiState.Idle -> Unit // do nothing
                }
            }
        }

        // Kick off the initial dashboard load
        vm.load()
    }
}

