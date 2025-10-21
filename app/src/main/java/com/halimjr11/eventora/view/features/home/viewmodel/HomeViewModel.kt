package com.halimjr11.eventora.view.features.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.domain.usecase.GetUpcomingUseCase
import com.halimjr11.eventora.utils.Constants
import com.halimjr11.eventora.utils.DomainResult
import com.halimjr11.eventora.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getUpcomingUseCase: GetUpcomingUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {
    private val _homeEvents =
        MutableStateFlow<UiState<Pair<List<EventDomain>, List<EventDomain>>>>(UiState.Loading)
    val homeEvents = _homeEvents.asStateFlow()

    init {
        loadHomeEvents()
    }

    fun loadHomeEvents() = viewModelScope.launch(dispatcher.io) {
        _homeEvents.value = when (val result = getUpcomingUseCase()) {
            is DomainResult.Success -> UiState.Success(result.data)
            else -> UiState.Error(Constants.DATA_ERROR)
        }
    }
}