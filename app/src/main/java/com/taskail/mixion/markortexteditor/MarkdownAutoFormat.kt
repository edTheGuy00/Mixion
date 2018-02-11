package com.taskail.mixion.markortexteditor

import android.text.InputFilter
import android.text.Spanned
import com.taskail.mixion.markortexteditor.highlighter.MarkdownHighlighterPattern
import java.lang.NumberFormatException

/**
 *Created by ed on 2/11/18.
 */
class MarkdownAutoFormat : InputFilter {

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {

        try {
            if (end.minus( start) == 1
                    && start < source.length
                    && dstart <= dest.length) {
                val newChar = source.get(start)

                if (newChar == '\n') {
                    return autoIndent(source, dest, dstart, dend)
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return source
    }

    private fun autoIndent(source: CharSequence, dest: Spanned, dstart: Int, dend: Int): CharSequence {
        val istart = findLineBreakPosition(dest, dstart)

        // append white space of previous line and new indent
        return source.toString() + createIndentForNextLine(dest, dend, istart)
    }

    private fun findLineBreakPosition(dest: Spanned, dstart: Int): Int {
        var istart = dstart.minus(1)

        while (istart > -1) {
            val c = dest[istart]
            if (c == '\n') {
                break
            }
            --istart
        }
        return istart
    }

    private fun createIndentForNextLine(dest: Spanned, dend: Int, isStart: Int): String{
        if (isStart > -1 && isStart < dest.length - 1) {


            isStart.inc()
            var iend = isStart

            while (iend < dest.length.minus(1)) {
                val c = dest[iend]
                if (c != ' ' && c != '\t') {
                    break
                }
                iend.inc()
            }

            if (iend < dest.length - 1) {
                // This is for any line that is not the first line in a file
                val listMatcher = MarkdownHighlighterPattern.LIST_UNORDERED.pattern.matcher(dest.toString().substring(iend, dend))
                if (listMatcher.find()) {
                    return dest.subSequence(isStart, iend).toString() + Character.toString(dest[iend]) + " "
                } else {
                    val m = MarkdownHighlighterPattern.LIST_ORDERED.pattern.matcher(dest.toString().substring(iend, dend))
                    return if (m.find()) {
                        dest.subSequence(isStart, iend).toString() + addNumericListItemIfNeeded(m.group(1))
                    } else {
                        ""
                    }
                }
            } else {
                return ""
            }
        } else if (isStart > -1) {
            return ""
        } else if (dest.length > 1) {
            val listMatcher = MarkdownHighlighterPattern.LIST_UNORDERED.pattern.matcher(dest.toString())
            if (listMatcher.find()) {
                return Character.toString(dest[0]) + " "
            } else {
                val m = MarkdownHighlighterPattern.LIST_ORDERED.pattern.matcher(dest.toString())
                return if (m.find()) {
                    addNumericListItemIfNeeded(m.group(1))
                } else {
                    ""
                }
            }
        } else {
            return ""
        }
    }

    private fun addNumericListItemIfNeeded(itemNumStr: String) : String{
        return try {
            val nextC = Integer.parseInt(itemNumStr) + 1
            nextC.toString() + ". "
        } catch (e: NumberFormatException) {
            // This should never ever happen
            ""
        }

    }
}