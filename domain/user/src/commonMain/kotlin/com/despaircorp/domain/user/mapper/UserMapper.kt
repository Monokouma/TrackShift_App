package com.despaircorp.domain.user.mapper

import com.despaircorp.domain.user.domain.model.User
import com.despaircorp.services.trackshift_api.service.dto.UserDto

fun UserDto.toDomain() = User(
    id = id,
    displayName = displayName,
    avatarUrl = avatarUrl,
    isPro = isPro,
    createdAt = createdAt,
    linksCreatedMonth = linksCreatedMonth,
    linksConvertedMonth = linksConvertedMonth,
    totalLinksCreated = totalLinksCreated,
    totalPlaylistsCreated = totalPlaylistsCreated,
    totalLinksConverted = totalLinksConverted,
    proExpiresAt = proExpiresAt,
    lastResetAt = lastResetAt,
)