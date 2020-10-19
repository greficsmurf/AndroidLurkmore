package com.app.lurkmoarapp.domain

import com.squareup.moshi.Json

//Page
data class PageContainer(
    val page: Page
)
data class Page(
    val id: Long,
    val title: String,
    val parsedWikiText: ParsedText?,
    val parsedText: ParsedText?,
    val sections: List<PageSection> = listOf()
)
data class ParsedText(
    val text: String?
)
data class PageSection(
    val tocLevel: Int,
    val level: Int,
    val line: String,
    val number: String,
    val index: String,
    val fromTitle: String,
    val byteOffset: Long
)



//Search
data class SearchResult(
    val searchResults: List<SearchItem>
)
data class SearchItem(
    val title: String,

    var isHistory: Boolean = false
)
//
//sealed class TextStyle{
//    data class Bold(val t: String ="") : TextStyle()
//    data class Normal(val t: String ="") : TextStyle()
//    data class Header(val t: String ="") : TextStyle()
//    data class Quote(val t: String ="") : TextStyle()
//    data class List(val t: String = "") : TextStyle()
//}