package com.taskail.mixion.markortexteditor.highlighter

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.InputFilter
import com.taskail.mixion.markortexteditor.MarkdownAutoFormat

/**
 *  Markor text Editor from https://github.com/gsantner/markor
 */

open class MarkdownHighlighter : Highlighter(){

    private val colors: MarkdownHighlighterColors = MarkdownHighlighterColors()
    internal open val fontType: String = "vidaloka"
    internal val fontSize: Int = 18
    private val highlightHexcolorEnabled: Boolean = false
    private val highlightLineEnding: Boolean = false
    private val highlightCodeChangeFont: Boolean = false

    override fun run(editor: HighlightingEditor, editable: Editable): Editable {

        try {
            clearSpans(editable)

            if (editable.isEmpty()) {
                return editable
            }

            profiler.start(true, "Markdown Highlighting")

            profiler.restart("Header")
            createHeaderSpanForMatches(editable, MarkdownHighlighterPattern.HEADER, colors.getHeaderColor())
            profiler.restart("Link")
            createColorSpanForMatches(editable, MarkdownHighlighterPattern.LINK.pattern, colors.getLinkColor())
            profiler.restart("List")
            createColorSpanForMatches(editable, MarkdownHighlighterPattern.LIST_UNORDERED.pattern, colors.getListColor())
            profiler.restart("OrderedList")
            createColorSpanForMatches(editable, MarkdownHighlighterPattern.LIST_ORDERED.pattern, colors.getListColor())
            if (highlightLineEnding) {
                profiler.restart("Double space ending - bgcolor")
                createColorBackgroundSpan(editable, MarkdownHighlighterPattern.DOUBLESPACE_LINE_ENDING.pattern, colors.getDoublespaceColor())
            }
            profiler.restart("Bold")
            createStyleSpanForMatches(editable, MarkdownHighlighterPattern.BOLD.pattern, Typeface.BOLD)
            profiler.restart("Italics")
            createStyleSpanForMatches(editable, MarkdownHighlighterPattern.ITALICS.pattern, Typeface.ITALIC)
            profiler.restart("Quotation")
            createColorSpanForMatches(editable, MarkdownHighlighterPattern.QUOTATION.pattern, colors.getQuotationColor())
            profiler.restart("Strikethrough")
            createSpanWithStrikeThroughForMatches(editable, MarkdownHighlighterPattern.STRIKETHROUGH.pattern)
            if (highlightCodeChangeFont) {
                profiler.restart("Code - Font [MonoSpace]")
                createMonospaceSpanForMatches(editable, MarkdownHighlighterPattern.CODE.pattern)
            }
            profiler.restart("Code - bgolor")
            createColorBackgroundSpan(editable, MarkdownHighlighterPattern.CODE.pattern, colors.getDoublespaceColor())
            if (highlightHexcolorEnabled) {
                profiler.restart("RGB Color underline")
                createColoredUnderlineSpanForMatches(editable, HexColorCodeUnderlineSpan.PATTERN, HexColorCodeUnderlineSpan(), 1)
            }

            profiler.end()
            profiler.printProfilingGroup()

        } catch (e: Exception) {
            // Ignoring errors
        }

        return editable

    }

    override fun getAutoFormatter(): InputFilter {
        return MarkdownAutoFormat()
    }

    override fun getHighlightingDelay(context: Context): Int {
        return 270
    }

    private fun createHeaderSpanForMatches(editable: Editable, pattern: MarkdownHighlighterPattern, headerColor: Int) {
        createSpanForMatches(editable, pattern.pattern, MarkdownHeaderSpanCreator(this, editable, headerColor), 0)
    }
}