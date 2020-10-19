package com.app.lurkmoarapp.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbPageSection(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val pageId: Long,
    val tocLevel: Int,
    val level: Int,
    val line: String,
    val number: String,
    val index: String,
    val fromTitle: String,
    val byteOffset: Long?
)