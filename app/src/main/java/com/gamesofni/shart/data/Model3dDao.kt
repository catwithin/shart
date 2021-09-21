package com.gamesofni.shart.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Model3dDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Model3d)

    suspend fun insertAll(vararg items: Model3d) {
        for (i in items) insert(i)
    }


    @Query("SELECT * from model3d WHERE id = :id")
    fun getItem(id: Int): Model3d
//    fun getItem(id: Int): Flow<Model3d>

    @Query("SELECT * from model3d ORDER BY name ASC")
    fun getItems(): List<Model3d>
//    fun getItems(): Flow<List<Model3d>>

    @Query("DELETE FROM model3d")
    fun deleteAll()
}
