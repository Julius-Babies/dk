package util.human_readable

data class LocaleNumberResources(
    val groupSeparator: String,
    val decimalSymbol: String
)

object NumberResources {
    val en = LocaleNumberResources(
        groupSeparator = ",",
        decimalSymbol = "."
    )

    val de = LocaleNumberResources(
        groupSeparator = ".",
        decimalSymbol = ","
    )

    private val registry: Map<String, LocaleNumberResources> = mapOf(
        "en" to en,
        "de" to de
    )

    private fun getLocale(id: String) = registry[id] ?: en

    fun getGroupSeparator(localeId: String): String {
        return getLocale(localeId).groupSeparator
    }

    fun getDecimalSymbol(localeId: String): String {
        return getLocale(localeId).decimalSymbol
    }
}

