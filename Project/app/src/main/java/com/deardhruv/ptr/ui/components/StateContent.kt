package com.deardhruv.ptr.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deardhruv.ptr.ui.common.UiState

/**
 * Renders every [UiState] branch so screens never show a blank or crash:
 *
 * ```
 * StateContent(state, onRetry = vm::refresh, emptyTitle = "No notes yet") { notes -> NoteList(notes) }
 * ```
 */
@Composable
fun <T> StateContent(
    state: UiState<T>,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    emptyTitle: String = "Nothing here yet",
    emptyMessage: String? = null,
    emptyAction: (@Composable () -> Unit)? = null,
    content: @Composable (T) -> Unit,
) {
    AnimatedContent(
        targetState = state,
        contentKey = { it::class },
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        modifier = modifier,
        label = "StateContent",
    ) { current ->
        when (current) {
            UiState.Loading -> LoadingState()
            UiState.Empty -> EmptyState(title = emptyTitle, message = emptyMessage, action = emptyAction)
            is UiState.Error -> ErrorState(message = current.message, onRetry = onRetry)
            is UiState.Success -> content(current.data)
        }
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyState(
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    icon: ImageVector = Icons.Outlined.Info,
    action: (@Composable () -> Unit)? = null,
) {
    MessageState(icon = icon, title = title, message = message, modifier = modifier, action = action)
}

@Composable
fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
) {
    MessageState(
        icon = Icons.Outlined.Warning,
        title = "Something went wrong",
        message = message,
        iconTint = MaterialTheme.colorScheme.error,
        modifier = modifier,
        action = onRetry?.let { retry -> { FilledTonalButton(onClick = retry) { Text("Try again") } } },
    )
}

@Composable
private fun MessageState(
    icon: ImageVector,
    title: String,
    message: String?,
    modifier: Modifier = Modifier,
    iconTint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    action: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(48.dp))
        Spacer(Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        if (message != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        if (action != null) {
            Spacer(Modifier.height(20.dp))
            action()
        }
    }
}
