package com.example.nit3213.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.example.nit3213.R
import com.example.nit3213.databinding.ActivityLoginBinding
import com.example.nit3213.ui.dashboard.DashboardActivity
import com.example.nit3213.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val vm: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // enable edge-to-edge layout
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ----- Apply status bar insets to top bar -----
        ViewCompat.setOnApplyWindowInsetsListener(binding.topAppBar) { v, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.updatePadding(top = top)
            insets
        }

        // ----- Login button -----
        binding.btnLogin.setOnClickListener {
            val campus = when (binding.groupCampus.checkedButtonId) {
                R.id.btnFootscray -> "footscray"
                R.id.btnSydney    -> "sydney"
                R.id.btnBrisbane  -> "br"
                else              -> "footscray"
            }

            val username = binding.etUsername.text?.toString().orEmpty()
            val password = binding.etPassword.text?.toString().orEmpty()

            vm.login(campus, username, password)
        }

        // ----- State collection -----
        lifecycleScope.launch {
            vm.state.collect { state ->
                when (state) {
                    is UiState.Idle -> {
                        binding.progress.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                    }
                    is UiState.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        binding.progress.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                        finish()
                    }
                    is UiState.Error -> {
                        binding.progress.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = state.message
                    }
                }
            }
        }
    }
}
