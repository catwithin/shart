package com.gamesofni.shart.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ShartObjectDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ShartObject)


    @Query("SELECT * from shart_object WHERE id = :id")
    fun getItem(id: Int): Flow<ShartObject>

    @Query("SELECT * from shart_object ORDER BY id ASC")
    fun getItems(): Flow<List<ShartObject>>
}
