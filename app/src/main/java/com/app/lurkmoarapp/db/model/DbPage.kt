package com.app.lurkmoarapp.db.model

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(primaryKeys = [
    "id",
    "title"
])
data class DbPage(
    val id: Long,
    val title: String,
    @ColumnInfo(name = "parsed_text")
    val parsedText: String?,
    val sections: List<DbPageSection>? = null
)