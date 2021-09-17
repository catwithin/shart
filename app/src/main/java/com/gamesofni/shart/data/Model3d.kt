package com.gamesofni.shart.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "model3d")
data class Model3d (

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val name: String,

    @ColumnInfo(name = "preview_resource_id")
    val previewResourceId: Int,

    @ColumnInfo(name = "model_obj_path")
    val filenameObj: String,

    @ColumnInfo(name = "texture_path")
    val textureResourcePath: String,
)

