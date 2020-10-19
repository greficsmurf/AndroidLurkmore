package com.app.lurkmoarapp.parser.models

import android.graphics.Typeface
import android.text.style.StyleSpan
import com.app.lurkmoarapp.parser.Parser

open class SpecialCharGroup(
    open val openingVal: String,
    open val closingVal: String = openingVal,
    val htmlTagName: String = "span",
    val appendString: String = "",
    val prependString: String = "",
    private val isNested: Boolean = false,   // to remove
    private val htmlTagList: List<String> = listOf(htmlTagName),
    private val htmlOpenTag: String = htmlTagList.joinToString("") { "<$it>" },
    private val htmlCloseTag: String = htmlTagList.reversed().joinToString("") { "</$it>" },

    val getSpans: (txt: String) -> List<SpanInfo> = { txt -> listOf(
        SpanInfo(
            StyleSpan(
                Typeface.NORMAL
            ), IntRange(0, txt.length)
        )
    )}
){
    fun wrapByHtml(txt: String): String{
        val matches = buildRegex().findAll(txt)
        var txtRet = txt
        for (match in matches){

            val pureVal = removeOpenCloseChars(match.value)
            val taggedVal = "$htmlOpenTag$prependString$pureVal$appendString$htmlCloseTag"

            txtRet = txtRet.replace(
                match.value,
                taggedVal
            )
        }

        return txtRet
    }

    fun findFirst(txt: String) = buildRegex().find(txt)?.value ?: ""

    private fun buildRegex() = if(isNested){
        ("(" + escapeRegexStr(openingVal) +")(.[^(" + escapeRegexStr(closingVal) + ")(" + escapeRegexStr(openingVal) + ")]*?)(" + escapeRegexStr(closingVal) + ")").toRegex()
    }
    else
    {
        ("(" + escapeRegexStr(openingVal) +")(((?!" + escapeRegexStr(closingVal) + ")(?!" + escapeRegexStr(openingVal) + ").)*?)(" + escapeRegexStr(closingVal) + ")").toRegex()
    }

    private fun removeOpenCloseChars(txt: String) = txt.replace(
        openingVal, ""
    ).replace(closingVal, "")

    protected fun escapeRegexStr(str: String) =
        str.replace("{", "\\{")
            .replace("}", "\\}")
            .replace("|", "\\|")
            .replace("\n", "\\\n")
            .replace("*", "\\*")
            .replace("[", "\\[")
            .replace("]", "\\]")
}