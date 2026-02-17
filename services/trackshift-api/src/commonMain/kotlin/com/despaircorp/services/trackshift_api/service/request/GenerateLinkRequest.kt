package com.despaircorp.services.trackshift_api.service.request

import kotlinx.serialization.Serializable

@Serializable
data class GenerateLinkRequest(
    val method: String,
    val images: List<ByteArray>,
    val url: String
)
