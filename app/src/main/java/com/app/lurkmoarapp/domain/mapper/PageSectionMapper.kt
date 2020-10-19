package com.app.lurkmoarapp.domain.mapper

import com.app.lurkmoarapp.db.model.DbPageSection
import com.app.lurkmoarapp.domain.PageSection


fun List<PageSection>.asDbModel(pageId: Long) = map {pageSection ->
    pageSection.asDbModel(pageId)
}



fun DbPageSection.asDomainModel() = PageSection(
    tocLevel,
    level,
    line,
    number,
    index,
    fromTitle,
    byteOffset ?: 0
)

fun List<DbPageSection>.asDomainModel() = map { dbPageSection ->
    dbPageSection.asDomainModel()
}
