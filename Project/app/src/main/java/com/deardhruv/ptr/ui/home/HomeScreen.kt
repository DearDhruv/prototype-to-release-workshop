package com.deardhruv.ptr.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.deardhruv.ptr.ui.components.EmptyState
import com.deardhruv.ptr.ui.components.PtrScreen

/**
 * Placeholder start destination. Replace its body with the workshop-day primary screen
 * (keep `PtrScreen` + `StateContent`).
 */
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    PtrScreen(title = "PTR", largeTitle = true, modifier = modifier) { padding ->
        Box(Modifier.padding(padding)) {
            EmptyState(
                title = "PTR Workshop Ready",
                message = "Skeleton in place. The live build starts here.",
                icon = Icons.Outlined.Build,
            )
        }
    }
}
