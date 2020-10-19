package com.app.lurkmoarapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.lurkmoarapp.db.model.DbPage
import kotlinx.coroutines.flow.Flow

@Dao
interface DbPageDao {
    @Query("SELECT * FROM DbPage")
    fun getAll(): List<DbPage>

    @Query("SELECT * FROM DbPage WHERE id=:id")
    fun getById(id: Long): DbPage

    @Query("SELECT * FROM DbPage WHERE UPPER(title) = UPPER(:title)")
    fun getByTitle(title: String): Flow<DbPage?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(page: DbPage)
}