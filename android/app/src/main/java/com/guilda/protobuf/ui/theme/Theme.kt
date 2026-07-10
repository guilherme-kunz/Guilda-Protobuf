package com.guilda.protobuf.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary              = Dark_Primary,
    onPrimary            = Dark_OnPrimary,
    primaryContainer     = Dark_PrimaryContainer,
    onPrimaryContainer   = Dark_OnPrimaryContainer,
    secondary            = Dark_Secondary,
    onSecondary          = Dark_OnSecondary,
    secondaryContainer   = Dark_SecondaryContainer,
    onSecondaryContainer = Dark_OnSecondaryContainer,
    tertiary             = Dark_Tertiary,
    onTertiary           = Dark_OnTertiary,
    tertiaryContainer    = Dark_TertiaryContainer,
    onTertiaryContainer  = Dark_OnTertiaryContainer,
    error                = Dark_Error,
    onError              = Dark_OnError,
    errorContainer       = Dark_ErrorContainer,
    onErrorContainer     = Dark_OnErrorContainer,
    background           = Dark_Background,
    onBackground         = Dark_OnBackground,
    surface              = Dark_Surface,
    onSurface            = Dark_OnSurface,
    surfaceVariant       = Dark_SurfaceVariant,
    onSurfaceVariant     = Dark_OnSurfaceVariant,
    outline              = Dark_Outline,
)

private val LightColorScheme = lightColorScheme(
    primary              = Light_Primary,
    onPrimary            = Light_OnPrimary,
    primaryContainer     = Light_PrimaryContainer,
    onPrimaryContainer   = Light_OnPrimaryContainer,
    secondary            = Light_Secondary,
    onSecondary          = Light_OnSecondary,
    secondaryContainer   = Light_SecondaryContainer,
    onSecondaryContainer = Light_OnSecondaryContainer,
    tertiary             = Light_Tertiary,
    onTertiary           = Light_OnTertiary,
    tertiaryContainer    = Light_TertiaryContainer,
    onTertiaryContainer  = Light_OnTertiaryContainer,
    error                = Light_Error,
    onError              = Light_OnError,
    errorContainer       = Light_ErrorContainer,
    onErrorContainer     = Light_OnErrorContainer,
    background           = Light_Background,
    onBackground         = Light_OnBackground,
    surface              = Light_Surface,
    onSurface            = Light_OnSurface,
    surfaceVariant       = Light_SurfaceVariant,
    onSurfaceVariant     = Light_OnSurfaceVariant,
    outline              = Light_Outline,
)

@Composable
fun GuildaProtobufTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content,
    )
}
