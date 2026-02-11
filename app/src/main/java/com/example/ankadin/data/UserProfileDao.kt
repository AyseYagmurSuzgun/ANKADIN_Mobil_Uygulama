package com.example.ankadin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ankadin.data.UserProfileEntity

@Dao
interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfileEntity)

    @Query("SELECT * FROM user_profile WHERE uid = :uid")
    suspend fun getProfile(uid: String): UserProfileEntity?
}
