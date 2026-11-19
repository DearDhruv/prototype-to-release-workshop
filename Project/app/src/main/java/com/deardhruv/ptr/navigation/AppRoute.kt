package com.deardhruv.ptr.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * All Navigation 3 destinations. Add one `@Serializable` key per screen, e.g.:
 *
 * ```
 * @Serializable data class DetailRoute(val id: String) : AppRoute
 * ```
 *
 * Then register it in `AppNavigation`'s `entryProvider`. Pass IDs, not whole objects.
 */
@Serializable
sealed interface AppRoute : NavKey

@Serializable
data object HomeRoute : AppRoute
