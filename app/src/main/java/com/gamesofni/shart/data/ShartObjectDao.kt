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

    suspend fun insertAll(vararg items: ShartObject) {
        for (i in items) insert(i)
    }

    @Query("SELECT * from shart_object WHERE id = :id")
    fun getItem(id: Int): ShartObject
//    fun getItem(id: Int): Flow<ShartObject>

    @Query("SELECT * from shart_object WHERE index_aug_img = :ai_id")
    fun getByAugImgId(ai_id: Int): ShartObject

    @Query("SELECT * from shart_object ORDER BY id ASC")
    fun getItems(): List<ShartObject>
//    fun getItems(): Flow<List<ShartObject>>

    @Query("DELETE FROM shart_object")
    fun deleteAll()
}
