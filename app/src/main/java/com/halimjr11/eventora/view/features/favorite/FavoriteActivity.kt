package com.halimjr11.eventora.view.features.favorite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.halimjr11.eventora.databinding.ActivityFavoriteBinding
import com.halimjr11.eventora.ui.helper.goToDetail
import com.halimjr11.eventora.ui.helper.launchAndCollect
import com.halimjr11.eventora.ui.helper.visibleIf
import com.halimjr11.eventora.utils.UiState
import com.halimjr11.eventora.view.adapters.VerticalEventAdapter
import com.halimjr11.eventora.view.features.favorite.viewmodel.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModel()
    private val verticalEventAdapter: VerticalEventAdapter by lazy {
        VerticalEventAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.favMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupUI()
        observeData()
    }

    private fun setupUI() = with(binding) {
        rvFavorite.apply {
            adapter = verticalEventAdapter
            layoutManager =
                LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
        }

        verticalEventAdapter.setOnClickCallback {
            goToDetail(it.id)
        }
    }

    private fun observeData() = with(viewModel) {
        launchAndCollect(favoriteEvent) { state ->
            binding.run {
                loadingFav.visibleIf(state is UiState.Loading)
                nsvFavContent.visibleIf(state is UiState.Success)
                evFav.visibleIf(state is UiState.Error)
            }
            when (state) {
                is UiState.Success -> {
                    val list = state.data
                    verticalEventAdapter.submitList(list)
                }

                is UiState.Error -> {
                    binding.evFav.setMessageAndCallback(state.message)
                }

                else -> Unit
            }
        }
    }
}