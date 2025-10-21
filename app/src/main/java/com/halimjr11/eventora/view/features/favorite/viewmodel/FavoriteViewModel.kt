package com.halimjr11.eventora.view.features.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.domain.repository.EventLocalRepository
import com.halimjr11.eventora.utils.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform

class FavoriteViewModel(
    localRepository: EventLocalRepository,
) : ViewModel() {
    val favoriteEvent: StateFlow<UiState<List<EventDomain>>> =
        localRepository.getAllEvents().transform {
            if (it.isNotEmpty()) {
                emit(UiState.Success(it))
            } else {
                emit(UiState.Error("No favorite events"))
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )
}