package dev.krazymanj.shopito.extension

import java.util.Locale

fun Double.round(): String =
    String.format(Locale.US, "%.2f", this)