package com.taskail.mixion.ui.markortexteditor.highlighter

import java.util.regex.Pattern

/**
 *  Markor text Editor from https://github.com/gsantner/markor
 */
enum class MarkdownHighlighterPattern {

   BOLD(Pattern.compile("(?<=(\\n|^|\\s))(([*_]){2,3})(?=\\S)(.*?)\\S\\2(?=(\\n|$|\\s))")),
   ITALICS(Pattern.compile("(?<=(\\n|^|\\s))([*_])(?=((?!\\2)|\\2{2,}))(?=\\S)(.*?)\\S\\2(?=(\\n|$|\\s))")),
   HEADER(Pattern.compile("(?m)((^#{1,6}[^\\S\\n][^\\n]+)|((\\n|^)[^\\s]+.*?\\n(-{2,}|={2,})[^\\S\\n]*$))")),
   LINK(Pattern.compile("\\[([^\\[]+)\\]\\(([^\\)]+)\\)")),
   LIST_UNORDERED(Pattern.compile("(\\n|^)\\s{0,3}([*+-])( \\[[ xX]\\])?(?= )")),
   LIST_ORDERED(Pattern.compile("(?m)^([0-9]+)(\\.)")),
   QUOTATION(Pattern.compile("(\\n|^)>")),
   STRIKETHROUGH(Pattern.compile("~{2}(.*?)\\S~{2}")),
   CODE(Pattern.compile("(?m)(`(.*?)`)|(^[^\\S\\n]{4}.*$)")),
   DOUBLESPACE_LINE_ENDING(Pattern.compile("(?m)(?<=\\S)([^\\S\\n]{2,})\\n"));

   internal lateinit var pattern: Pattern

   constructor(pattern: Pattern){
      this.pattern = pattern
   }

}