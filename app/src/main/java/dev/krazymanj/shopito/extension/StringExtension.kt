package dev.krazymanj.shopito.extension

val String.Companion.empty: String
    get() = ""


fun String.ellipsis(limit: Int, suffix: String = "..."): String {
    if (this.length <= limit) {
        return this
    }
    return "${this.take(limit)}$suffix"
}