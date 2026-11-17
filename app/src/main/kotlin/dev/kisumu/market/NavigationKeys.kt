package dev.kisumu.market

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Market : NavKey

@Serializable data class Detail(val id: Long) : NavKey

@Serializable data object Tips : NavKey
