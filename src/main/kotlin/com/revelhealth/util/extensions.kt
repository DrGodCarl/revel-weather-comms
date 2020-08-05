package com.revelhealth.util

import com.natpryce.Failure
import com.natpryce.Result
import com.natpryce.Success

/**
 * Result-safe way to unwrap a value from a map
 */
fun <K, V, E> Map<K, V>.maybeGet(key: K, error: () -> E): Result<V, E> {
    return this[key]?.let { Success(it) } ?: Failure(error())
}

/**
 * A special case of the above.
 */
fun <K, V> Map<K, V>.maybeGet(key: K): Result<V, RuntimeException> {
    return this.maybeGet(key) { RuntimeException("They key $key was missing from the map.") }
}
