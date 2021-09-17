package com.gamesofni.shart.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "shart_object")
data class ShartObject(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "model_id")
    val modelId: Int,

    @ColumnInfo(name = "index_aug_img")
    val indexAugmentedImg: Int,

    @ColumnInfo(name = "global_id_aug_img")
    val globalAugmentedImgId: Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "is_private")
    val isPrivate: Boolean,

//    @ColumnInfo(name = "added_on")
//    val addedOn: Date?,
)
