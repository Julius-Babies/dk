package util.human_readable

data class PluralForms(
    val one: String,
    val other: String,
    val futureOther: String? = null,
    val pastOther: String? = null
)

data class LocaleTimeResources(
    val plurals: Map<String, PluralForms>,
    val strings: Map<String, String>
)

data class LocaleFileSizeResources(
    val units: Map<String, String>
)

enum class Tense { NONE, PAST, FUTURE }

object TimeResources {
    val en = LocaleTimeResources(
        plurals = mapOf(
            "seconds" to PluralForms(one = "second", other = "seconds"),
            "minutes" to PluralForms(one = "minute", other = "minutes"),
            "hours"   to PluralForms(one = "hour",   other = "hours"),
            "days"    to PluralForms(one = "day",    other = "days"),
            "weeks"   to PluralForms(one = "week",   other = "weeks"),
            "months"  to PluralForms(one = "month",  other = "months"),
            "years"   to PluralForms(one = "year",   other = "years")
            // future/past overrides können hier pro Einheit als futureOther/pastOther gesetzt werden
        ),
        strings = mapOf(
            "time_ago" to "{time} ago",
            "time_in_future" to "in {time}",
            "now" to "now"
        )
    )

    // Registry: weitere Sprachen hier hinzufügen, z.B. "de" to deResources
    val registry: Map<String, LocaleTimeResources> = mapOf(
        "en" to en
    )

    private fun getLocale(id: String) = registry[id] ?: en

    fun formatTime(localeId: String, unit: String, count: Long, tense: Tense = Tense.NONE): String {
        val res = getLocale(localeId)
        if (count == 0L) return res.strings["now"] ?: "now"

        val plural = res.plurals[unit] ?: res.plurals["seconds"]!! // fallback
        val unitWord = if (count == 1L) plural.one else plural.other
        val base = "$count $unitWord"

        return when (tense) {
            Tense.PAST -> {
                val override = plural.pastOther?.let { if (count == 1L) plural.one else it }
                if (!override.isNullOrBlank()) {
                    val text = "$count $override"
                    res.strings["time_ago"]?.replace("{time}", text) ?: text
                } else {
                    res.strings["time_ago"]?.replace("{time}", base) ?: base
                }
            }
            Tense.FUTURE -> {
                val override = plural.futureOther?.let { if (count == 1L) plural.one else it }
                if (!override.isNullOrBlank()) {
                    val text = "$count $override"
                    res.strings["time_in_future"]?.replace("{time}", text) ?: text
                } else {
                    res.strings["time_in_future"]?.replace("{time}", base) ?: base
                }
            }
            Tense.NONE -> base
        }
    }
}

object FileSizeResources {
    val en = LocaleFileSizeResources(
        units = mapOf(
            "byte" to "B",
            "kilobyte" to "kB",
            "megabyte" to "MB",
            "gigabyte" to "GB",
            "terabyte" to "TB"
        )
    )

    val registry: Map<String, LocaleFileSizeResources> = mapOf(
        "en" to en
    )

    private fun getLocale(id: String) = registry[id] ?: en

    fun getUnit(localeId: String, unit: String): String {
        return getLocale(localeId).units[unit] ?: unit
    }
}
