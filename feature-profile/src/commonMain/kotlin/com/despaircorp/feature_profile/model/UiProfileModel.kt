package com.despaircorp.feature_profile.model

import com.despaircorp.domain.user.domain.model.User
import kotlin.Int
import kotlin.time.Clock

data class UiProfileModel(
    val id: String,
    val username: String,
    val imageUrl: String,
    val isPro: Boolean,
    val totalLinksCreated: Int,
    val totalPlaylistsCreated: Int,
    val totalLinksConverted: Int,
    val proExpiresAt: String?,
    val linksCreatedMonth: Int,
    val linksConvertedMonth: Int,
)

fun User.toUiModel(): UiProfileModel = UiProfileModel(
    id = id,
    username = displayName,
    imageUrl = "$avatarUrl?t=${Clock.System.now().epochSeconds}",
    isPro = isPro,
    totalLinksCreated = linksCreatedMonth,
    totalPlaylistsCreated = linksConvertedMonth,
    totalLinksConverted = totalLinksCreated,
    proExpiresAt = proExpiresAt,
    linksCreatedMonth = linksCreatedMonth,
    linksConvertedMonth = linksConvertedMonth,
)