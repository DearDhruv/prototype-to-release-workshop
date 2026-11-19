package com.deardhruv.ptr.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Standard screen shell: edge-to-edge (`safeDrawing` insets), themed background, top bar with
 * optional back arrow, actions, FAB, bottom bar and snackbars. Every screen should use it so
 * insets and transitions stay consistent.
 *
 * Use [largeTitle] = true for top-level screens (collapsing large app bar) and false for detail screens.
 * Pass the `contentPadding` to your `LazyColumn` (see [PaddingValues.plus]) so content scrolls under the bars.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PtrScreen(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    largeTitle: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable (contentPadding: PaddingValues) -> Unit,
) {
    val scrollBehavior = if (largeTitle) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    } else {
        TopAppBarDefaults.pinnedScrollBehavior()
    }
    val barColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
    )
    val navigationIcon: @Composable () -> Unit = {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    }
    val titleText: @Composable () -> Unit = {
        Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            if (largeTitle) {
                LargeTopAppBar(
                    title = titleText,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    scrollBehavior = scrollBehavior,
                    colors = barColors,
                )
            } else {
                TopAppBar(
                    title = titleText,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    scrollBehavior = scrollBehavior,
                    colors = barColors,
                )
            }
        },
        floatingActionButton = floatingActionButton,
        bottomBar = bottomBar,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = content,
    )
}

/** Adds extra spacing on top of Scaffold padding, e.g. `LazyColumn(contentPadding = padding + 16.dp)`. */
@Composable
operator fun PaddingValues.plus(extra: Dp): PaddingValues {
    val direction = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(direction) + extra,
        top = calculateTopPadding() + extra,
        end = calculateEndPadding(direction) + extra,
        bottom = calculateBottomPadding() + extra,
    )
}
