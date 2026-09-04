package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalysisHistoryDao {
    @Query("SELECT * FROM analysis_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<AnalysisHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: AnalysisHistoryEntity): Long

    @Update
    suspend fun update(entry: AnalysisHistoryEntity)

    @Query("DELETE FROM analysis_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM analysis_history")
    suspend fun clearAll()
}
