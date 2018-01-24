package com.taskail.mixion.bottomNav

import android.util.SparseArray

/**
 *Created by ed on 1/24/18.
 */
class EnumCodeMap<T>(enumeration: Class<T>) where T : Enum<T>, T : EnumCode{

    private val map: SparseArray<T>

    init {
        map = codeToEnumMap(enumeration)
    }

    operator fun get(code: Int): T {
        return map.get(code) ?: throw IllegalArgumentException("code=" + code)
    }

    private fun codeToEnumMap(enumeration: Class<T>): SparseArray<T> {
        val ret = SparseArray<T>()
        for (value in enumeration.enumConstants) {
            ret.put(value.code(), value)
        }
        return ret
    }

    fun size(): Int {
        return map.size()
    }
}