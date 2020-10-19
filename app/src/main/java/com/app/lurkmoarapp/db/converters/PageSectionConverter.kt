package com.app.lurkmoarapp.db.converters

import androidx.room.TypeConverter
import com.app.lurkmoarapp.db.model.DbPageSection
import com.beust.klaxon.Klaxon
import com.squareup.moshi.Moshi
import org.json.JSONArray

class PageSectionConverter {
    @TypeConverter
    fun fromSections(list: List<DbPageSection>) = Klaxon().toJsonString(list)

    @TypeConverter
    fun toSections(str: String): List<DbPageSection>? = Klaxon().parseArray(str)
}