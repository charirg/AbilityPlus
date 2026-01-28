package com.charirodriguez.abilityplus.data.local.dao.seed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.charirodriguez.abilityplus.data.local.entity.seed.ActividadBvdEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadBvdDao {

    @Query("SELECT * FROM actividad_bvd ORDER BY id ASC")
    fun getAll(): Flow<List<ActividadBvdEntity>>

    @Query("SELECT COUNT(*) FROM actividad_bvd")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ActividadBvdEntity>)
}
