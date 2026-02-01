package com.charirodriguez.abilityplus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.charirodriguez.abilityplus.data.local.entity.PersonaCifEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonaCifDao {

    @Query("SELECT * FROM persona_cif WHERE personaId = :personaId")
    fun getByPersona(personaId: Long): Flow<List<PersonaCifEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: PersonaCifEntity)

    @Query("DELETE FROM persona_cif WHERE personaId = :personaId AND cifCodigo = :cifCodigo")
    suspend fun delete(personaId: Long, cifCodigo: String)
}
