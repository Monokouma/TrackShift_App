package com.despaircorp.domain.user.mapper

import com.despaircorp.domain.user.domain.model.User
import com.despaircorp.services.trackshift_api.service.dto.UserDto

fun UserDto.toDomain(): User? = User(
    id = id,
    displayName = displayName ?: return null,
    avatarUrl = avatarUrl.orEmpty(),
    isPro = isPro ?: false,
    createdAt = createdAt ?: return null,
    linksCreatedMonth = linksCreatedMonth ?: 0,
    linksConvertedMonth = linksConvertedMonth ?: 0,
    totalLinksCreated = totalLinksCreated ?: 0,
    totalPlaylistsCreated = totalPlaylistsCreated ?: 0,
    totalLinksConverted = totalLinksConverted ?: 0,
    proExpiresAt = proExpiresAt,
    lastResetAt = lastResetAt,
)