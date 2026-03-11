package com.jght.business.mobility.mymowiapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform