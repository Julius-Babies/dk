// https://github.com/jacobras/Human-Readable/blob/29831e80456524d960ac862659f558e45bdd06c6/src/commonMain/kotlin/nl/jacobras/humanreadable/HumanReadableRelativeTime.kt

package util.human_readable

import util.detectLocale
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private fun formatDuration(
    duration: Duration,
    relativeTime: RelativeTime,
    localeId: String = detectLocale()
): String {
    val secs = duration.inWholeSeconds
    if (secs == 0L) return TimeResources.formatTime(localeId, "seconds", 0L, Tense.NONE)

    val (count, unit) = when {
        secs < 60 -> secs to "seconds"
        secs < 60 * 60 -> (secs / 60) to "minutes"
        secs < 60 * 60 * 24 -> (secs / (60 * 60)) to "hours"
        secs < 60 * 60 * 24 * 7 -> (secs / (60 * 60 * 24)) to "days"
        secs < 60L * 60 * 24 * 30 -> (secs / (60 * 60 * 24 * 7)) to "weeks"
        secs < 60L * 60 * 24 * 365 -> (secs / (60 * 60 * 24 * 30)) to "months"
        else -> (secs / (60 * 60 * 24 * 365)) to "years"
    }

    val tense = if (relativeTime == RelativeTime.Past) Tense.PAST else Tense.FUTURE
    return TimeResources.formatTime(localeId, unit, count, tense)
}

/**
 * Returns the difference between [baseInstant] and [instant], in human-readable format.
 * Also supports instants in the future.
 *
 * @param instant The [Instant] to compare with [baseInstant].
 * @param baseInstant The base/starting [Instant], usually "now".
 */
@OptIn(ExperimentalTime::class)
internal fun formatTimeAgo(
    instant: Instant,
    baseInstant: Instant
): String {
    val localeId = detectLocale()
    val diff = baseInstant - instant
    val secondsAgo = diff.inWholeSeconds

    return when {
        secondsAgo < 0 -> formatDuration(diff.absoluteValue, RelativeTime.Future, localeId)
        secondsAgo <= 1 -> TimeResources.formatTime(localeId, "seconds", 0L, Tense.NONE)
        else -> formatDuration(diff.absoluteValue, RelativeTime.Past, localeId)
    }
}