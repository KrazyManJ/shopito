package dev.krazymanj.shopito.extension

fun <T> LinkedHashSet<T>.removeFirstItem() {
    val iterator = this.iterator()
    if (iterator.hasNext()) {
        iterator.next()
        iterator.remove()
    }
}