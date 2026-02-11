package com.example.ankadin.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ankadin.data.AcilKisiEntity

@Dao
interface AcilKisiDao {

    @Query("SELECT * FROM AcilKisiEntity WHERE uid = :uid")
    suspend fun getAll(uid: String): List<AcilKisiEntity>

    @Query("SELECT * FROM AcilKisiEntity WHERE id = :id")
    suspend fun getById(id: Int): AcilKisiEntity?

    @Insert
    suspend fun insert(acilKisi: AcilKisiEntity)

    @Update
    suspend fun update(acilKisi: AcilKisiEntity)

    @Delete
    suspend fun delete(acilKisi: AcilKisiEntity)

    @Query("SELECT COUNT(*) FROM AcilKisiEntity WHERE uid = :uid")
    suspend fun count(uid: String): Int
}
