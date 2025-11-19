@file:OptIn(ExperimentalForeignApi::class)

package util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

private const val forceEnglish = true

fun detectLocale(): String {
    if (forceEnglish) return "en"
    val envPtr = getenv("LC_ALL") ?: getenv("LANG") ?: getenv("LANGUAGE")
    val raw = envPtr?.toKString()
    return raw?.substringBefore('.')?.substringBefore('_')?.ifEmpty { "en" } ?: "en"
}