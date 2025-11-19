package util.human_readable

import util.detectLocale
import kotlin.math.pow
import kotlin.math.roundToLong

internal fun Double.formatNumber(
    decimals: Int
): String {
    val localeId = detectLocale()
    val groupSeparator = NumberResources.getGroupSeparator(localeId).formatWithSpaceIfNeeded()
    val decimalSymbol = NumberResources.getDecimalSymbol(localeId)
    val rounded = formatWithDecimals(decimals)
    val parts = rounded.split('.')

    // Format the integer part
    val formattedIntegerPart = parts[0]
        .reversed()
        .chunked(3)
        .joinToString(groupSeparator)
        .reversed()

    // Format the decimal part
    val decimalPart = if (parts.size > 1) parts[1] else ""
    val formattedDecimalPart = if (decimals > 0) {
        val truncatedDecimals = decimalPart.padEnd(decimals, '0').substring(0, decimals)
        decimalSymbol + truncatedDecimals
    } else {
        ""
    }

    return formattedIntegerPart + formattedDecimalPart
}

private fun Double.formatWithDecimals(decimals: Int): String {
    val multiplier = 10.0.pow(decimals)
    val numberAsString = (this * multiplier).roundToLong().toString().padStart(decimals + 1, '0')
    val decimalIndex = numberAsString.length - decimals - 1
    val mainRes = numberAsString.substring(0..decimalIndex)
    val fractionRes = numberAsString.substring(decimalIndex + 1)
    return if (fractionRes.isEmpty()) {
        mainRes
    } else {
        "$mainRes.$fractionRes"
    }
}

/**
 * Workaround for empty group separator returning a space.
 */
private fun String.formatWithSpaceIfNeeded(): String {
    return ifEmpty { " " }
}