package com.charirodriguez.abilityplus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.charirodriguez.abilityplus.data.local.entity.PersonaDiagnosticoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonaDiagnosticoDao {

    @Query("SELECT * FROM persona_diagnostico WHERE personaId = :personaId")
    fun getByPersona(personaId: Long): Flow<PersonaDiagnosticoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: PersonaDiagnosticoEntity)
}
