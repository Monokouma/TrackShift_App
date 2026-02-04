package com.despaircorp.design_system.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

// =============================================================================
// ELECTRIC PULSE - Color Schemes
// Dark-first design for immersive music experience
// =============================================================================

val DarkColorScheme = darkColorScheme(
    // Primary: Electric Violet - Brand identity, main actions
    primary = ElectricViolet60,
    onPrimary = ElectricViolet10,
    primaryContainer = ElectricViolet20,
    onPrimaryContainer = ElectricViolet90,

    // Secondary: Lime - Energy, success, power actions
    secondary = Lime60,
    onSecondary = Lime10,
    secondaryContainer = Lime20,
    onSecondaryContainer = Lime90,

    // Tertiary: Electric Blue - Links, utility, interactive
    tertiary = ElectricBlue60,
    onTertiary = ElectricBlue10,
    tertiaryContainer = ElectricBlue20,
    onTertiaryContainer = ElectricBlue90,

    // Error: Hot Coral
    error = Coral60,
    onError = Coral10,
    errorContainer = Coral20,
    onErrorContainer = Coral90,

    // Background & Surface: Pure black foundation
    background = Neutral0,
    onBackground = Neutral90,
    surface = Neutral0,
    onSurface = Neutral90,
    surfaceVariant = Neutral17,
    onSurfaceVariant = Neutral70,
    surfaceDim = Neutral0,
    surfaceBright = Neutral22,
    surfaceContainerLowest = Neutral0,
    surfaceContainerLow = Neutral6,
    surfaceContainer = Neutral10,
    surfaceContainerHigh = Neutral17,
    surfaceContainerHighest = Neutral22,

    // Outline
    outline = Neutral40,
    outlineVariant = Neutral24,

    // Inverse
    inverseSurface = Neutral90,
    inverseOnSurface = Neutral10,
    inversePrimary = ElectricViolet40,

    // Misc
    scrim = Neutral0,
    surfaceTint = ElectricViolet60,
)

val LightColorScheme = lightColorScheme(
    // Primary: Electric Violet
    primary = ElectricViolet50,
    onPrimary = Neutral100,
    primaryContainer = ElectricViolet90,
    onPrimaryContainer = ElectricViolet10,

    // Secondary: Lime
    secondary = Lime50,
    onSecondary = Neutral100,
    secondaryContainer = Lime90,
    onSecondaryContainer = Lime10,

    // Tertiary: Electric Blue
    tertiary = ElectricBlue50,
    onTertiary = Neutral100,
    tertiaryContainer = ElectricBlue90,
    onTertiaryContainer = ElectricBlue10,

    // Error: Hot Coral
    error = Coral50,
    onError = Neutral100,
    errorContainer = Coral95,
    onErrorContainer = Coral10,

    // Background & Surface
    background = Neutral99,
    onBackground = Neutral10,
    surface = Neutral99,
    onSurface = Neutral10,
    surfaceVariant = Neutral94,
    onSurfaceVariant = Neutral30,
    surfaceDim = Neutral87,
    surfaceBright = Neutral99,
    surfaceContainerLowest = Neutral100,
    surfaceContainerLow = Neutral96,
    surfaceContainer = Neutral95,
    surfaceContainerHigh = Neutral92,
    surfaceContainerHighest = Neutral90,

    // Outline
    outline = Neutral50,
    outlineVariant = Neutral80,

    // Inverse
    inverseSurface = Neutral20,
    inverseOnSurface = Neutral95,
    inversePrimary = ElectricViolet80,

    // Misc
    scrim = Neutral0,
    surfaceTint = ElectricViolet50,
)
