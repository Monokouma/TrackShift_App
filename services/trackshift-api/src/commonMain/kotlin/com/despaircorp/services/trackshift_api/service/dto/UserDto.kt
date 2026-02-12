package com.despaircorp.services.trackshift_api.service.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    @SerialName("display_name")
    val displayName: String?,
    @SerialName("avatar_url")
    val avatarUrl: String?,
    @SerialName("is_pro")
    val isPro: Boolean,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("links_created_month")
    val linksCreatedMonth: Int,
    @SerialName("links_converted_month")
    val linksConvertedMonth: Int,
    @SerialName("total_links_created")
    val totalLinksCreated: Int,
    @SerialName("total_playlists_created")
    val totalPlaylistsCreated: Int,
    @SerialName("total_links_converted")
    val totalLinksConverted: Int,
    @SerialName("pro_expires_at")
    val proExpiresAt: String?,
    @SerialName("last_reset_at")
    val lastResetAt: String?
)
