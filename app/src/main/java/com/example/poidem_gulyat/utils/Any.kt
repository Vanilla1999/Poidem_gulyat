package com.example.poidem_gulyat.utils

inline fun <reified T> Any?.tryCast(block: () -> Unit) {
    if (this   is T) {
        block()
    }
}
inline fun <reified T> List<*>.asListOfType(noinline block: List<T>.() -> Unit) {
    if (all { it is T })
        block
}
