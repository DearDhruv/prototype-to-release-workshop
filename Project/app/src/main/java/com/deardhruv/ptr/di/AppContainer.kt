package com.deardhruv.ptr.di

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.deardhruv.ptr.PtrApplication
import kotlinx.serialization.json.Json

private val Context.ptrDataStore: DataStore<Preferences> by preferencesDataStore(name = "ptr_store")

/**
 * App-wide dependencies. Add repositories here as `by lazy` properties, e.g.:
 *
 * ```
 * val noteRepository by lazy {
 *     NoteRepository(JsonStore(dataStore, json, "notes", ListSerializer(Note.serializer()), emptyList()))
 * }
 * ```
 */
class AppContainer(context: Context) {
    val appContext: Context = context.applicationContext

    /** Single offline-first persistence store for the whole app (see `data/local/JsonStore`). */
    val dataStore: DataStore<Preferences> = appContext.ptrDataStore

    val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = false
    }
}

val Context.appContainer: AppContainer
    get() = (applicationContext as PtrApplication).container

/**
 * Creates a ViewModel with access to [AppContainer]. Scoped to the current Navigation 3 entry
 * (via the ViewModelStore entry decorator in `AppNavigation`), so it is cleared when the screen is popped.
 *
 * ```
 * val vm = ptrViewModel { NotesViewModel(noteRepository) }
 * ```
 */
@Composable
inline fun <reified VM : ViewModel> ptrViewModel(
    key: String? = null,
    crossinline create: AppContainer.() -> VM,
): VM {
    val container = LocalContext.current.appContainer
    return viewModel(
        key = key,
        factory = viewModelFactory { initializer { container.create() } },
    )
}
