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

    // ViewBinding gives us easy access to all views in activity_login.xml
    private lateinit var binding: ActivityLoginBinding

    // ViewModel holds the login logic and state
    private val vm: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // This makes the app use the whole screen, even behind the status bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adjust the toolbar padding so it doesn't overlap the system status bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.topAppBar) { v, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.updatePadding(top = top)
            insets
        }

        // ---- When user taps "Login" ----
        binding.btnLogin.setOnClickListener {
            // Get the chosen campus from the radio button group
            val campus = when (binding.groupCampus.checkedButtonId) {
                R.id.btnFootscray -> "footscray"
                R.id.btnSydney    -> "sydney"
                R.id.btnBrisbane  -> "br"
                else              -> "footscray" // fallback if none is chosen
            }

            // Grab the typed username & password
            val username = binding.etUsername.text?.toString().orEmpty()
            val password = binding.etPassword.text?.toString().orEmpty()

            // Tell the ViewModel to attempt login
            vm.login(campus, username, password)
        }

        // ---- Watch the ViewModel for login results ----
        lifecycleScope.launch {
            vm.state.collect { state ->
                when (state) {
                    is UiState.Idle -> {
                        // nothing happening
                        binding.progress.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                    }
                    is UiState.Loading -> {
                        // show spinner while waiting for server
                        binding.progress.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        // login worked → move to Dashboard
                        binding.progress.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                        finish() // close login so back button won’t return here
                    }
                    is UiState.Error -> {
                        // login failed → show error text
                        binding.progress.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = state.message
                    }
                }
            }
        }
    }
}

