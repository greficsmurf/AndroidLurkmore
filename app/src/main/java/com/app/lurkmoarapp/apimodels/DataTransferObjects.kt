package com.app.lurkmoarapp.apimodels

import com.app.lurkmoarapp.api.qualifiers.ApiSectionTitle
import com.app.lurkmoarapp.domain.PageContainer
import com.squareup.moshi.Json
import dagger.multibindings.IntoMap


data class ApiPageContainer(
    @Json(name = "parse")
    val page: ApiPage?
)
data class ApiPage(
    @Json(name = "revid")
    val id: Long,
    val title: String,
    val sections: List<ApiPageSection>,
    @Json(name="wikitext")
    val parsedWikiText: ApiParsedText?,
    @Json(name="text")
    val parsedText: ApiParsedText?
)
data class ApiParsedText(
    @Json(name="*")
    val text: String?
)

data class ApiPageSection(
    @Json(name = "toclevel")
    val tocLevel: Int,
    val level: Int,
    val line: String,
    val number: String,
    val index: String,
    @Json(name = "fromtitle")
    val fromTitleStr: Any = "",
    val fromTitle: String = fromTitleStr.toString(),
    @Json(name = "byteoffset")
    val byteOffset: Long?
)