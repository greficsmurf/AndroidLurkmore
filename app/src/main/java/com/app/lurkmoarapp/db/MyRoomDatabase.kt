package com.app.lurkmoarapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.app.lurkmoarapp.db.converters.PageSectionConverter
import com.app.lurkmoarapp.db.dao.DbPageDao
import com.app.lurkmoarapp.db.dao.DbPageSectionDao
import com.app.lurkmoarapp.db.dao.DbSearchItemDao
import com.app.lurkmoarapp.db.model.DbPage
import com.app.lurkmoarapp.db.model.DbPageSection
import com.app.lurkmoarapp.db.model.DbSearchItem

@Database(
    entities = [
        DbPage::class,
        DbSearchItem::class,
        DbPageSection::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(PageSectionConverter::class)
abstract class MyRoomDatabase : RoomDatabase(){
    abstract fun pageDao(): DbPageDao

    abstract fun searchItemDao(): DbSearchItemDao

    abstract fun pageSectionDao(): DbPageSectionDao

}