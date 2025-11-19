// https://github.com/jacobras/Human-Readable/blob/29831e80456524d960ac862659f558e45bdd06c6/src/commonMain/kotlin/nl/jacobras/humanreadable/HumanReadableFileSize.kt
package util.human_readable

import util.detectLocale

/**
 * Returns the given [bytes] size in human-readable format.
 */
internal fun formatFileSize(bytes: Long, decimals: Int): String {
    val localeId = detectLocale()
    return when {
        bytes < 1024 -> {
            "$bytes ${FileSizeResources.getUnit(localeId, "byte")}"
        }
        bytes < 1_048_576 -> {
            "${(bytes / 1_024.0).formatNumber(decimals)}${FileSizeResources.getUnit(localeId, "kilobyte")}"
        }
        bytes < 1.07374182E9 -> {
            "${(bytes / 1_048_576.0).formatNumber(decimals)}${FileSizeResources.getUnit(localeId, "megabyte")}"
        }
        bytes < 1.09951163E12 -> {
            "${(bytes / 1.07374182E9).formatNumber(decimals)}${FileSizeResources.getUnit(localeId, "gigabyte")}"
        }
        else -> {
            "${(bytes / 1.09951163E12).formatNumber(decimals)}${FileSizeResources.getUnit(localeId, "terabyte")}"
        }
    }
}
