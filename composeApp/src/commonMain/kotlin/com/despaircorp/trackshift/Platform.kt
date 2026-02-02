package com.despaircorp.trackshift

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform