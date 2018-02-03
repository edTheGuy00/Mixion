package com.taskail.mixion.utils

import org.jsoup.Jsoup
import java.io.IOException
import java.util.regex.Pattern

/**
 *Created by ed on 1/24/18.
 */

/**
 * this extracts a certain number of characters for a given string.
 */

@Throws(IOException::class, IndexOutOfBoundsException::class)
fun extractShortSummary(begin: Int, length: Int, s: String): String {
    return s.substring(begin, Math.min(s.length, length))
}

/**
 * Jsoup parser parses an html body and keeps line breaks
 */
fun jsoupParser(body: String): String{
    val newBody = Jsoup.parse(body.replace("(?i)<br[^>]*>", "br2n")).text()
    return newBody.replace("br2n", "\n")
}

private fun containsText(match: String, s: String): Boolean {
    return s.contains(match)
}

private fun containsMarkdown(s: String): Boolean {
    return s.indexOf('[') > -1
}

private fun extractMarkdown(body: String){
    val location = lastLocation(body)
}

private fun lastLocation(s: String): Int {
    return s.indexOf(')')
}

private fun removeUrl(input: String): String? {

    var withoutLink: String? = null

    val urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"
    val p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE)
    val m = p.matcher(input)
    var i = 0
    while (m.find()) {
        withoutLink = input.replace(Pattern.quote(m.group(i)).toRegex(), "").trim { it <= ' ' }
        i++
    }
    return withoutLink
}