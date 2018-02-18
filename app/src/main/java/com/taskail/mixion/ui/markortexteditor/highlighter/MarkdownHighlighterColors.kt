package com.taskail.mixion.ui.markortexteditor.highlighter

/**
 *  Markor text Editor from https://github.com/gsantner/markor
 */
class MarkdownHighlighterColors {
    private val COLOR_HEADER = -0x109300
    private val COLOR_LINK = -0xe15c02
    private val COLOR_LIST = -0x255adf
    private val COLOR_QUOTE = -0x774fb4
    private val COLOR_DOUBLESPACE = -0x1f1e20

    fun getHeaderColor(): Int {
        return COLOR_HEADER
    }

    fun getLinkColor(): Int {
        return COLOR_LINK
    }

    fun getListColor(): Int {
        return COLOR_LIST
    }

    fun getDoublespaceColor(): Int {
        return COLOR_DOUBLESPACE
    }

    fun getQuotationColor(): Int {
        return COLOR_QUOTE
    }
}