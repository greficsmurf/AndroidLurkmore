package com.app.lurkmoarapp.parser.models

class SectionDividerCharGroup(
    override val openingVal: String,
    override val closingVal: String = openingVal,

    val level: Int
) : SpecialCharGroup(
    openingVal,
    closingVal
) {
    fun buildSectionRegex() =
        ("(${escapeRegexStr(openingVal)})(.*?)(${escapeRegexStr(closingVal)})(.*?)(?" + escapeRegexStr(openingVal) + ")")
            .toRegex()

    //(==)(.*?)(==)(.*?)(?==)
}