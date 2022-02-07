package gr.atcom.gpslocationservice.model.common

import android.os.Parcelable
import gr.atcom.gpslocationservice.model.common.Model.Companion.INVALID_BOOLEAN
import gr.atcom.gpslocationservice.model.common.Model.Companion.INVALID_DOUBLE
import gr.atcom.gpslocationservice.model.common.Model.Companion.INVALID_INT
import gr.atcom.gpslocationservice.model.common.Model.Companion.INVALID_LONG
import gr.atcom.gpslocationservice.model.common.Model.Companion.INVALID_STRING

interface Model: Parcelable {
    companion object {
        const val INVALID_LONG = -1L
        const val INVALID_DOUBLE = 0.0
        const val INVALID_INT = -1
        const val INVALID_STRING = ""
        const val INVALID_BOOLEAN = false
    }
}

fun Double?.nonNull(defaultValue: Double = INVALID_DOUBLE): Double {
    if (this == null) {
        return defaultValue
    }
    return this
}

fun Long?.nonNull(defaultValue: Long = INVALID_LONG): Long {
    if (this == null) {
        return defaultValue
    }
    return this
}

fun Int?.nonNull(defaultValue: Int = INVALID_INT): Int {
    if (this == null) {
        return defaultValue
    }
    return this
}

fun String?.nonNull(defaultValue: String = INVALID_STRING): String {
    if (this == null) {
        return defaultValue
    }
    return this
}


fun Boolean?.nonNull(defaultValue: Boolean = INVALID_BOOLEAN): Boolean {
    if (this == null) {
        return defaultValue
    }
    return this
}