package com.app.lurkmoarapp.db.model

data class DbPageAndPageSection(
    val page: DbPage,
    val pageSections: List<DbPageSection>
)