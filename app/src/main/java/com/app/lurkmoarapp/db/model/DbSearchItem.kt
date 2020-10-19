package com.app.lurkmoarapp.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.lurkmoarapp.markers.DatabaseEntity

@Entity
data class DbSearchItem(
    val title: String,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "modified_at") var modifiedAt: Long = System.currentTimeMillis()
) : DatabaseEntity