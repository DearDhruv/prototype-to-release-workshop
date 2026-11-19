package com.deardhruv.ptr.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import com.deardhruv.ptr.ui.home.HomeScreen

private const val NAV_DURATION_MS = 350

/** Material 3 "emphasized decelerate" easing. */
private val EmphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)

/*
 * Transitions are opaque slides (no crossfade). A crossfade makes both screens semi-transparent
 * mid-animation, exposing the window behind NavDisplay and causing a flash on every push/pop.
 */
private fun <T : Any> AnimatedContentTransitionScope<Scene<T>>.pushTransition(): ContentTransform =
    slideInHorizontally(tween(NAV_DURATION_MS, easing = EmphasizedDecelerate)) { it } togetherWith
        slideOutHorizontally(tween(NAV_DURATION_MS, easing = EmphasizedDecelerate)) { -it / 4 }

private fun <T : Any> AnimatedContentTransitionScope<Scene<T>>.popTransition(): ContentTransform =
    ContentTransform(
        targetContentEnter = slideInHorizontally(tween(NAV_DURATION_MS, easing = EmphasizedDecelerate)) { -it / 4 },
        initialContentExit = slideOutHorizontally(tween(NAV_DURATION_MS, easing = EmphasizedDecelerate)) { it },
        // Keep the screen being dismissed on top while it slides away.
        targetContentZIndex = -1f,
    )

/**
 * App navigation host (Navigation 3). To add a screen:
 * 1. Add a `@Serializable` key to `AppRoute.kt`.
 * 2. Register it below: `entry<DetailRoute> { key -> DetailScreen(id = key.id, onBack = backStack::removeLastOrNull) }`.
 * 3. Navigate with `backStack.add(DetailRoute(id))`; go back with `backStack.removeLastOrNull()`.
 */
@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(HomeRoute)

    // Opaque, theme-colored backdrop so the window background never shows through during transitions.
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                // Scopes `ptrViewModel { ... }` to each back stack entry; cleared when the entry is popped.
                rememberViewModelStoreNavEntryDecorator(),
            ),
            transitionSpec = { pushTransition() },
            popTransitionSpec = { popTransition() },
            predictivePopTransitionSpec = { _ -> popTransition() },
            entryProvider = entryProvider<NavKey> {
                entry<HomeRoute> { HomeScreen() }
            },
        )
    }
}
