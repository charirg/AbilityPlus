package com.charirodriguez.abilityplus.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.charirodriguez.abilityplus.data.local.entity.PersonaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonaDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(persona: PersonaEntity): Long

    @Update
    suspend fun update(persona: PersonaEntity)

    @Delete
    suspend fun delete(persona: PersonaEntity)

    @Query("SELECT * FROM persona WHERE id = :id")
    suspend fun getById(id: Long): PersonaEntity?

    @Query("SELECT * FROM persona WHERE activo = 1 ORDER BY numeroExpediente ASC")
    fun getActivas(): Flow<List<PersonaEntity>>

    @Query("SELECT * FROM persona WHERE activo = 0 ORDER BY numeroExpediente ASC")
    fun getEliminadas(): Flow<List<PersonaEntity>>

    @Query("UPDATE persona SET ultimoInformeMillis = :millis WHERE id = :personaId")
    suspend fun setUltimoInforme(personaId: Long, millis: Long)
    @Query("UPDATE persona SET fechaValoracionMillis = :millis WHERE id = :personaId")
    suspend fun setFechaValoracion(personaId: Long, millis: Long)


}

