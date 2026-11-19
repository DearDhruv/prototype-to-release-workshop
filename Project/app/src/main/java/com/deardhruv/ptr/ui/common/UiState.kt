package com.deardhruv.ptr.ui.common

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/** Screen state for Unidirectional Data Flow. Render it with `StateContent`. */
@Immutable
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data object Empty : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

/**
 * Maps a data [Flow] to [UiState]: `Empty` when [isEmpty] is true, `Error` on exceptions.
 * Lists and collections are treated as empty when they have no elements.
 */
fun <T> Flow<T>.asUiState(
    isEmpty: (T) -> Boolean = { (it as? Collection<*>)?.isEmpty() == true },
    errorMessage: (Throwable) -> String = { it.message ?: "Something went wrong" },
): Flow<UiState<T>> =
    map<T, UiState<T>> { if (isEmpty(it)) UiState.Empty else UiState.Success(it) }
        .catch { emit(UiState.Error(errorMessage(it))) }

/**
 * ViewModel helper: `val state = repo.items.asUiState().stateInUi(viewModelScope)`.
 * Starts as `Loading`, stops collecting 5s after the UI goes away (survives rotation).
 */
fun <T> Flow<UiState<T>>.stateInUi(scope: CoroutineScope): StateFlow<UiState<T>> =
    stateIn(scope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)
