package com.deardhruv.ptr.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.IOException

/**
 * Offline-first, type-safe persistence for any `@Serializable` value (typically a `List<Model>`),
 * stored as JSON under one DataStore key. Corrupt or unreadable data falls back to [default]
 * instead of crashing, so the app always launches.
 *
 * ```
 * val store = JsonStore(dataStore, json, "notes", ListSerializer(Note.serializer()), default = SEED_NOTES)
 * store.data                      // Flow<List<Note>>
 * store.update { it + newNote }   // atomic read-modify-write
 * ```
 */
class JsonStore<T>(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
    key: String,
    private val serializer: KSerializer<T>,
    private val default: T,
) {
    private val prefKey = stringPreferencesKey(key)

    val data: Flow<T> = dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { prefs -> prefs[prefKey]?.let(::decodeOrDefault) ?: default }
        .distinctUntilChanged()

    suspend fun update(transform: (T) -> T) {
        dataStore.edit { prefs ->
            val current = prefs[prefKey]?.let(::decodeOrDefault) ?: default
            prefs[prefKey] = json.encodeToString(serializer, transform(current))
        }
    }

    suspend fun set(value: T) = update { value }

    suspend fun clear() {
        dataStore.edit { it.remove(prefKey) }
    }

    private fun decodeOrDefault(raw: String): T =
        try {
            json.decodeFromString(serializer, raw)
        } catch (_: SerializationException) {
            default
        } catch (_: IllegalArgumentException) {
            default
        }
}
