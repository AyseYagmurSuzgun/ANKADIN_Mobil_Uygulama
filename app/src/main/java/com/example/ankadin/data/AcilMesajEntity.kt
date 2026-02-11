package com.example.ankadin.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "acil_mesaj",
    foreignKeys = [
        ForeignKey(
            entity = AcilKisiEntity::class,
            parentColumns = ["id"],
            childColumns = ["kisiId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AcilMesajEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val kisiId: Int,
    val mesaj: String,
    val tarih: Long = System.currentTimeMillis()
)
