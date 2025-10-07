package com.halimjr11.eventora.ui.helper

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.view.features.detail.DetailActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun View.visibleIf(state: Boolean) {
    this.isVisible = state
}

fun View.gone() {
    this.isVisible = false
}

fun Int?.orZero(): Int = this ?: 0

/**
 * Extension to show available quota safely.
 */
fun EventDomain.getQuotaText(): String {
    return when {
        quota == 0 -> "Full"
        quota <= 5 -> "Only $quota seats left!"
        else -> "There is $quota available"
    }
}

fun <T> LifecycleOwner.launchAndCollect(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    collector: suspend (T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state) {
            flow.collectLatest(collector)
        }
    }
}

fun Context.goToDetail(id: Int) {
    val intent = Intent(this, DetailActivity::class.java).apply {
        putExtra(DetailActivity.DETAIL_ID, id)
    }
    startActivity(intent)
}