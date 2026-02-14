package com.despaircorp.domain.user.domain.model

import kotlinx.serialization.SerialName

data class User(
    val id: String,
    val displayName: String,
    val avatarUrl: String,
    val isPro: Boolean,
    val createdAt: String,
    val linksCreatedMonth: Int,
    val linksConvertedMonth: Int,
    val totalLinksCreated: Int,
    val totalPlaylistsCreated: Int,
    val totalLinksConverted: Int,
    val proExpiresAt: String?,
    val lastResetAt: String?
)
