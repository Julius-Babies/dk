// https://github.com/jacobras/Human-Readable/blob/29831e80456524d960ac862659f558e45bdd06c6/src/commonMain/kotlin/nl/jacobras/humanreadable/RelativeTime.kt

package util.human_readable

/**
 * Indicates in what time frame the requested time unit needs
 * to be localised. Used to support grammar cases in languages like German.
 */
internal enum class RelativeTime {
    Past, Future
}