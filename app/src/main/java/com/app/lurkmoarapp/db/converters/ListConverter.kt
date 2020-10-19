package com.app.lurkmoarapp.db.converters

import androidx.room.TypeConverter
import com.app.lurkmoarapp.db.model.DbPageSection
import com.beust.klaxon.Klaxon

class ListConverter {

    @TypeConverter
    fun <T> fromSections(list: List<T>) = Klaxon().toJsonString(list)

    @TypeConverter
    inline fun <reified T> toSections(str: String): List<T>? = Klaxon().parseArray<T>(str)
}