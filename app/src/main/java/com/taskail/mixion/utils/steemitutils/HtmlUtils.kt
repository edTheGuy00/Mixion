/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taskail.mixion.utils.steemitutils

import android.content.res.ColorStateList
import android.os.Build
import android.support.annotation.ColorInt
import android.text.Html
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.URLSpan
import android.text.util.Linkify
import android.widget.TextView

import `in`.uncod.android.bypass.Bypass
import `in`.uncod.android.bypass.style.TouchableUrlSpan
import com.taskail.mixion.utils.GlideImageGetter
import com.taskail.mixion.utils.LinkTouchMovementMethod

/**
 *Created by ed on 1/27/18.
 */


/**
 * Work around some 'features' of TextView and URLSpans. i.e. vanilla URLSpans do not react to
 * touch so we replace them with our own [TouchableUrlSpan]
 * & [LinkTouchMovementMethod] to fix this.
 *
 *
 * Setting a custom MovementMethod on a TextView also alters touch handling (see
 * TextView#fixFocusableAndClickableSettings) so we need to correct this.
 */
fun setTextWithNiceLinks(textView: TextView, input: CharSequence) {
    textView.text = input
    textView.movementMethod = LinkTouchMovementMethod.getMovementInstance()
    textView.isFocusable = false
    textView.isClickable = false
    textView.isLongClickable = false
}

/**
 * Parse the given input using [TouchableUrlSpan]s rather than vanilla [URLSpan]s
 * so that they respond to touch.
 */
fun parseHtml(
        input: String,
        linkTextColor: ColorStateList,
        @ColorInt linkHighlightColor: Int): SpannableStringBuilder {
    var spanned = fromHtml(input)

    // strip any trailing newlines
    while (spanned[spanned.length.minus(1)] == '\n') {
        spanned = spanned.delete(spanned.length.minus( 1), spanned.length)
    }

    return linkifyPlainLinks(spanned, linkTextColor, linkHighlightColor)
}

fun parseAndSetText(textView: TextView, input: String) {
    if (TextUtils.isEmpty(input)) return
    setTextWithNiceLinks(textView, parseHtml(input, textView.linkTextColors,
            textView.highlightColor))
}

private fun linkifyPlainLinks(
        input: CharSequence,
        linkTextColor: ColorStateList,
        @ColorInt linkHighlightColor: Int): SpannableStringBuilder {
    val plainLinks = SpannableString(input) // copy of input

    // Linkify doesn't seem to work as expected on M+
    // TODO: figure out why
    Linkify.addLinks(plainLinks, Linkify.WEB_URLS)

    val urlSpans = plainLinks.getSpans(0, plainLinks.length, URLSpan::class.java)

    //TODO - figure out why this doesn't work
    // add any plain links to the output
    val ssb = SpannableStringBuilder(input)
    for (urlSpan in urlSpans) {
        ssb.removeSpan(urlSpan)
        ssb.setSpan(TouchableUrlSpan(urlSpan.url, linkTextColor, linkHighlightColor),
                plainLinks.getSpanStart(urlSpan),
                plainLinks.getSpanEnd(urlSpan),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    return ssb
}

fun fromHtml(input: String): SpannableStringBuilder {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY) as SpannableStringBuilder
    } else {
        Html.fromHtml(input) as SpannableStringBuilder
    }
}

fun fromHtml(input: String, imageGetter: GlideImageGetter) : Spanned{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY, imageGetter, null)
    } else {
        Html.fromHtml(input, imageGetter, null)
    }
}

/**
 * Parse Markdown and plain-text links.
 *
 *
 * [Bypass] does not handle plain text links (i.e. not md syntax) and requires a
 * `String` input (i.e. squashes any spans). [Linkify] handles plain links but also
 * removes any existing spans. So we can't just run our input through both.
 *
 *
 * Instead we use the markdown lib, then take a copy of the output and Linkify
 * **that**. We then find any [URLSpan]s and add them to the markdown output.
 * Best of both worlds.
 */
fun parseMarkdownAndPlainLinks(
        textView: TextView,
        input: String,
        markdown: Bypass,
        loadImageCallback: Bypass.LoadImageCallback): CharSequence {
    val markedUp = markdown.markdownToSpannable(input, textView, loadImageCallback)
    return linkifyPlainLinks(
            markedUp, textView.linkTextColors, textView.highlightColor)
}

/**
 * Parse Markdown and plain-text links and set on the [TextView] with proper clickable
 * spans. [input] should be either a parsed html or plain body depending on the feedtype
 */
fun parseMarkdownAndSetText(
        textView: TextView,
        input: String,
        markdown: Bypass,
        loadImageCallback: Bypass.LoadImageCallback) {
    if (TextUtils.isEmpty(input)) return

    setTextWithNiceLinks(textView,
            parseMarkdownAndPlainLinks(textView, input, markdown, loadImageCallback))
}
