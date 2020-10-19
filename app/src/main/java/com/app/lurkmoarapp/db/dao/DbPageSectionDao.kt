package com.app.lurkmoarapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.app.lurkmoarapp.db.model.DbPageSection

@Dao
interface DbPageSectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sections: List<DbPageSection>)

}