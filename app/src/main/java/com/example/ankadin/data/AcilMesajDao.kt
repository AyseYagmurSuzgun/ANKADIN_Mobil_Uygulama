package com.example.ankadin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AcilMesajDao {

    @Insert
    suspend fun insert(acilMesaj: AcilMesajEntity)

    @Query("SELECT * FROM acil_mesaj WHERE kisiId = :kisiId ORDER BY id DESC")
    suspend fun getMesajlarByKisiId(kisiId: Int): List<AcilMesajEntity>

}
