package com.lifestyle

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LifestyleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userTable: UserData)
}