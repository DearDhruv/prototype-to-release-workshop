package com.deardhruv.ptr.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/*
 * PTR brand palette — "Prototype → Production".
 * Electric indigo (primary) for action, deep teal (secondary) for progress/shipped,
 * warm amber (tertiary) for in-progress/attention. Full Material 3 role sets so every
 * component (cards, chips, top bars, surface containers) is on-brand in light and dark.
 */

// Light
private val IndigoLight = Color(0xFF4F46C8)
private val OnIndigoLight = Color(0xFFFFFFFF)
private val IndigoContainerLight = Color(0xFFE2DFFF)
private val OnIndigoContainerLight = Color(0xFF140B6E)
private val TealLight = Color(0xFF00687A)
private val OnTealLight = Color(0xFFFFFFFF)
private val TealContainerLight = Color(0xFFAEECFF)
private val OnTealContainerLight = Color(0xFF001F26)
private val AmberLight = Color(0xFF8B5000)
private val OnAmberLight = Color(0xFFFFFFFF)
private val AmberContainerLight = Color(0xFFFFDCBE)
private val OnAmberContainerLight = Color(0xFF2C1600)

// Dark
private val IndigoDark = Color(0xFFC3C0FF)
private val OnIndigoDark = Color(0xFF2A1FA0)
private val IndigoContainerDark = Color(0xFF3A31B0)
private val OnIndigoContainerDark = Color(0xFFE2DFFF)
private val TealDark = Color(0xFF56D6F4)
private val OnTealDark = Color(0xFF003640)
private val TealContainerDark = Color(0xFF004E5C)
private val OnTealContainerDark = Color(0xFFAEECFF)
private val AmberDark = Color(0xFFFFB870)
private val OnAmberDark = Color(0xFF4A2800)
private val AmberContainerDark = Color(0xFF693C00)
private val OnAmberContainerDark = Color(0xFFFFDCBE)

/** Window/backdrop colors — must match `res/values{,-night}/colors.xml` to avoid launch/transition flashes. */
internal val BackgroundLight = Color(0xFFFBF8FF)
internal val BackgroundDark = Color(0xFF131318)

internal val PtrLightColorScheme = lightColorScheme(
    primary = IndigoLight,
    onPrimary = OnIndigoLight,
    primaryContainer = IndigoContainerLight,
    onPrimaryContainer = OnIndigoContainerLight,
    inversePrimary = IndigoDark,
    secondary = TealLight,
    onSecondary = OnTealLight,
    secondaryContainer = TealContainerLight,
    onSecondaryContainer = OnTealContainerLight,
    tertiary = AmberLight,
    onTertiary = OnAmberLight,
    tertiaryContainer = AmberContainerLight,
    onTertiaryContainer = OnAmberContainerLight,
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = BackgroundLight,
    onBackground = Color(0xFF1B1B21),
    surface = BackgroundLight,
    onSurface = Color(0xFF1B1B21),
    surfaceVariant = Color(0xFFE4E1EC),
    onSurfaceVariant = Color(0xFF47464F),
    surfaceTint = IndigoLight,
    inverseSurface = Color(0xFF303036),
    inverseOnSurface = Color(0xFFF3EFF7),
    outline = Color(0xFF787680),
    outlineVariant = Color(0xFFC8C5D0),
    scrim = Color(0xFF000000),
    surfaceBright = BackgroundLight,
    surfaceDim = Color(0xFFDBD9E0),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF5F2FA),
    surfaceContainer = Color(0xFFEFECF4),
    surfaceContainerHigh = Color(0xFFEAE7EF),
    surfaceContainerHighest = Color(0xFFE4E1E9),
)

internal val PtrDarkColorScheme = darkColorScheme(
    primary = IndigoDark,
    onPrimary = OnIndigoDark,
    primaryContainer = IndigoContainerDark,
    onPrimaryContainer = OnIndigoContainerDark,
    inversePrimary = IndigoLight,
    secondary = TealDark,
    onSecondary = OnTealDark,
    secondaryContainer = TealContainerDark,
    onSecondaryContainer = OnTealContainerDark,
    tertiary = AmberDark,
    onTertiary = OnAmberDark,
    tertiaryContainer = AmberContainerDark,
    onTertiaryContainer = OnAmberContainerDark,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = BackgroundDark,
    onBackground = Color(0xFFE4E1E9),
    surface = BackgroundDark,
    onSurface = Color(0xFFE4E1E9),
    surfaceVariant = Color(0xFF47464F),
    onSurfaceVariant = Color(0xFFC8C5D0),
    surfaceTint = IndigoDark,
    inverseSurface = Color(0xFFE4E1E9),
    inverseOnSurface = Color(0xFF303036),
    outline = Color(0xFF928F9A),
    outlineVariant = Color(0xFF47464F),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFF39383F),
    surfaceDim = BackgroundDark,
    surfaceContainerLowest = Color(0xFF0E0E13),
    surfaceContainerLow = Color(0xFF1B1B21),
    surfaceContainer = Color(0xFF1F1F25),
    surfaceContainerHigh = Color(0xFF2A292F),
    surfaceContainerHighest = Color(0xFF35343A),
)
