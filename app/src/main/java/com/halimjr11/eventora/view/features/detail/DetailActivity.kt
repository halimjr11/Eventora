package com.halimjr11.eventora.view.features.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.google.android.material.color.MaterialColors
import com.halimjr11.eventora.databinding.ActivityDetailBinding
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.ui.helper.getQuotaText
import com.halimjr11.eventora.ui.helper.launchAndCollect
import com.halimjr11.eventora.ui.helper.visibleIf
import com.halimjr11.eventora.utils.UiState
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(binding.detailScroll.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val id = intent.getIntExtra(DETAIL_ID, 0)
        binding.progressCircular.run {
            isIndeterminate = true
            show()
        }
        viewModel.setId(id)
        observeData()
    }

    private fun observeData() = viewModel.run {
        launchAndCollect(viewModel.detailEvent) { state ->
            when (state) {
                is UiState.Success -> setupDataDetail(state.data)
                is UiState.Error -> {
                    binding.evDetailEvent.setMessageAndCallback(state.message) {
                        loadDetailEvent()
                    }
                }

                else -> {}
            }
            binding.run {
                detailScroll.visibleIf(state is UiState.Success)
                loadingOverlay.visibleIf(state is UiState.Loading)
                evDetailEvent.visibleIf(state is UiState.Error)
            }
        }
    }

    private fun setupDataDetail(data: EventDomain) = binding.run {
        ivCover.load(data.mediaCover)
        tvEventTitle.text = data.name
        tvSummary.text = data.summary
        tvDescription.text =
            HtmlCompat.fromHtml(data.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
        tvOwner.text = buildString {
            append(data.ownerName)
            append(" • ")
            append(data.category)
            append(" • ")
            append(data.cityName)
        }
        tvDate.text = data.beginTime
        tvQuota.run {
            val check = when {
                data.quota == 0 -> MaterialColors.getColor(
                    this,
                    com.google.android.material.R.attr.colorOnError
                )

                data.quota <= 5 -> MaterialColors.getColor(
                    this,
                    com.google.android.material.R.attr.colorOnSecondary
                )

                else -> MaterialColors.getColor(
                    this,
                    com.google.android.material.R.attr.colorOnPrimary
                )

            }
            text = data.getQuotaText()
            setTextColor(check)

        }
    }

    companion object {
        const val DETAIL_ID = "detail_id"
    }
}