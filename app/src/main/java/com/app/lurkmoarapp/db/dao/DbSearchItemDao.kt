package com.app.lurkmoarapp.db.dao

import androidx.room.*
import com.app.lurkmoarapp.db.model.DbSearchItem
import kotlinx.coroutines.flow.Flow

@Dao
interface DbSearchItemDao{

    @Query("SELECT * FROM DbSearchItem ORDER BY modified_at DESC LIMIT :count")
    fun getLastItems(count: Int = 5): Flow<List<DbSearchItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: DbSearchItem)

    suspend fun insertWithTimeStamp(item: DbSearchItem){
        insert(
            item.apply {
                modifiedAt = System.currentTimeMillis()
            }
        )
    }

    @Query("DELETE FROM DbSearchItem")
    suspend fun deleteAll()
}