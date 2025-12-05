package dev.krazymanj.shopito.extension

val String.Companion.empty: String
    get() = ""


fun String.ellipsis(limit: Int, suffix: String = "..."): String {
    if (this.length <= limit) {
        return this
    }
    return "${this.take(limit)}$suffix"
}

fun String.extractLastAmount(): Pair<String, Int> {

    val regex = Regex("\\b(x\\d+|\\d+x)\\b", RegexOption.IGNORE_CASE)

    val matches = regex.findAll(this)

    val lastMatch = matches.lastOrNull() ?: return Pair(this, 1)

    val numberPart = lastMatch.value.replace("x", "", ignoreCase = true)
    val parsedNumber = numberPart.toIntOrNull() ?: 1

    val range = lastMatch.range

    val textBefore = this.substring(0, range.first).trim()
    val textAfter = this.substring(range.last + 1).trim()

    val cleanText = "$textBefore $textAfter".trim()

    return Pair(cleanText, parsedNumber)

}