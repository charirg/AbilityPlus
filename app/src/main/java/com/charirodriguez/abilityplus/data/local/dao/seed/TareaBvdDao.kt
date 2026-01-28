package com.charirodriguez.abilityplus.data.local.dao.seed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.charirodriguez.abilityplus.data.local.entity.seed.TareaBvdEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaBvdDao {

    @Query("SELECT * FROM tarea_bvd WHERE actividadId = :actividadId ORDER BY id ASC")
    fun getByActividad(actividadId: Int): Flow<List<TareaBvdEntity>>

    @Query("SELECT COUNT(*) FROM tarea_bvd")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TareaBvdEntity>)
}
