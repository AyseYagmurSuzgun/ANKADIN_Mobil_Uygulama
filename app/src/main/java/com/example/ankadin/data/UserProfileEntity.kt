package com.example.ankadin.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(

    @PrimaryKey
    val uid: String,

    // Kişisel Bilgiler
    val isim: String?,
    val kimlikNo: String?,
    val cinsiyet: String?,
    val yas: Int?,
    val dogumTarihi: String?,
    val telefon: String?,

    // Sağlık Bilgileri
    val kanGrubu: String?,
    val alerjiler: String?,
    val ilaclar: String?,
    val hastaliklar: String?
)
