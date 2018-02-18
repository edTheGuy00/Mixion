package com.taskail.mixion.ui.markortexteditor

import java.text.DecimalFormat

/**
 * A timer for quick time measurement. Nano - in both, time and functions
 *
 * Markor text Editor from https://github.com/gsantner/markor
 */

class NanoProfiler {

    private val formatter = DecimalFormat("000000000.0000000")
    private var profilingGroupValue: Long = 0
    private var groupCount = 0
    private var profilerEnabled = true
    private var profilingValue: Long = -1
    private var text: String? = null

    fun setEnabled(enabled: Boolean): NanoProfiler {
        profilerEnabled = enabled
        return this
    }

    fun start(increaseGroupCounter: Boolean, vararg optionalText: String) {
        if (profilerEnabled) {
            if (increaseGroupCounter) {
                groupCount++
                profilingGroupValue = 0
            }
            text = if (optionalText != null && optionalText.size == 1) optionalText[0] else "action"
            profilingValue = System.nanoTime()
        }
    }

    fun restart(vararg optionalText: String) {
        end()
        start(false, *optionalText)
    }

    fun printProfilingGroup() {
        if (profilerEnabled) {
            val text = formatter.format((profilingGroupValue / 1000f).toDouble()).replace("\\G0".toRegex(), " ") + " [ms] for Group " + groupCount
            println("NanoProfiler::: " + groupCount + text)
        }
    }

    fun end() {
        val now = System.nanoTime()
        if (profilerEnabled) {
            profilingValue = now - profilingValue
            profilingGroupValue += (profilingValue / 1000f).toLong()
            val text = formatter.format((profilingValue / 1000f).toDouble()).replace("\\G0".toRegex(), " ") + " [µs] for " + text
            println("NanoProfiler::: " + groupCount + text)
        }
    }
}