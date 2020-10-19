package com.app.lurkmoarapp.domain.mapper

import com.app.lurkmoarapp.apimodels.ApiPage
import com.app.lurkmoarapp.apimodels.ApiPageContainer
import com.app.lurkmoarapp.apimodels.ApiPageSection
import com.app.lurkmoarapp.apimodels.ApiParsedText
import com.app.lurkmoarapp.db.model.DbPage
import com.app.lurkmoarapp.db.model.DbPageSection
import com.app.lurkmoarapp.domain.Page
import com.app.lurkmoarapp.domain.PageContainer
import com.app.lurkmoarapp.domain.PageSection
import com.app.lurkmoarapp.domain.ParsedText

fun ApiPageContainer.asDomainModel(): PageContainer{
    return PageContainer(
        page?.asDomainModel() ?: Page(
            -1, "", ParsedText(null), ParsedText(null)
        )
    )
}

fun ApiPage.asDomainModel() = Page(
    id,
    title,
    parsedWikiText?.asDomainModel(),
    parsedText?.asDomainModel(),
    sections.asDomainModel()
)

fun ApiParsedText.asDomainModel() = ParsedText(
    text
)

fun List<ApiPageSection>.asDomainModel() = map {
    it.asDomainModel()
}

fun ApiPageSection.asDomainModel() = PageSection(
    tocLevel,
    level,
    line,
    number,
    index,
    fromTitle,
    byteOffset ?: -1
)

fun PageSection.asDbModel(pageId: Long) = DbPageSection(
    pageId = pageId,
    tocLevel = tocLevel,
    level = level,
    line = line,
    number = number,
    index = index,
    fromTitle = fromTitle,
    byteOffset = byteOffset
)

// Database mapping

fun DbPage.asDomainModel() = Page(
    id,
    title,
    ParsedText(
        parsedText.orEmpty()
    ),
    null,
    sections?.asDomainModel() ?: listOf()
)

fun PageContainer.asDbModel(): DbPage{
    return page.asDbModel()
}

fun Page.asDbModel() = DbPage(
    id,
    title,
    parsedWikiText?.text,
    sections.asDbModel(id)
)