package com.example.ankadin.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AcilKisiEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uid: String,
    val isim: String,
    val telefon: String,
    val resimUri: String? = null
)
