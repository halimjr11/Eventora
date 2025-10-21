package com.halimjr11.eventora.view.features.search

import androidx.recyclerview.widget.LinearLayoutManager
import com.halimjr11.eventora.R
import com.halimjr11.eventora.databinding.FragmentSearchBinding
import com.halimjr11.eventora.ui.base.BaseFragment
import com.halimjr11.eventora.ui.helper.goToDetail
import com.halimjr11.eventora.ui.helper.launchAndCollect
import com.halimjr11.eventora.ui.helper.visibleIf
import com.halimjr11.eventora.utils.UiState
import com.halimjr11.eventora.view.adapters.SearchAdapter
import com.halimjr11.eventora.view.features.search.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment :
    BaseFragment<FragmentSearchBinding, SearchViewModel>(FragmentSearchBinding::inflate) {
    override val viewModel: SearchViewModel by viewModel()
    private val searchAdapter: SearchAdapter by lazy {
        SearchAdapter()
    }

    override fun setupUI() = with(binding) {
        progressCircular.run {
            isIndeterminate = true
            show()
        }
        rvEventList.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        super.setupUI()
    }

    override fun setupListeners() = with(binding) {
        searchView.setupWithSearchBar(searchBar)
        searchView
            .editText
            .setOnEditorActionListener { textView, actionId, event ->
                searchBar.setText(searchView.text)
                searchView.hide()
                viewModel.setQuerySearch(searchView.text.toString())
                false
            }
        searchAdapter.setOnClickCallback { event ->
            context?.goToDetail(event.id)
        }
        super.setupListeners()
    }

    override fun observeData() = with(viewModel) {
        launchAndCollect(searchEvents) { state ->
            when (state) {
                is UiState.Success -> searchAdapter.submitList(state.data)
                is UiState.Error -> {
                    binding.evSearchEvent.setMessageAndCallback(state.message) {
                        loadSearchEvents()
                    }
                }

                is UiState.Idle -> {
                    val message = context?.getString(R.string.search_placeholder).orEmpty()
                    binding.evSearchEvent.setMessageNoButton(message)
                }

                else -> {}
            }
            binding.run {
                loadingOverlay.visibleIf(state is UiState.Loading)
                rvEventList.visibleIf(state is UiState.Success)
                evSearchEvent.visibleIf(state is UiState.Error || state is UiState.Idle)
            }
        }
        super.observeData()
    }
}