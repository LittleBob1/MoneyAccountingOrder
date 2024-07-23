package com.awaken.appm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform