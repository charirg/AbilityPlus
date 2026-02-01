package com.charirodriguez.abilityplus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.charirodriguez.abilityplus.data.local.entity.ValoracionActividadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ValoracionActividadDao {

    @Query("""
        SELECT * FROM valoracion_actividad
        WHERE personaId = :personaId
    """)
    fun getByPersona(personaId: Long): Flow<List<ValoracionActividadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(valoracion: ValoracionActividadEntity)
}
