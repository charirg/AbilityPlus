package com.charirodriguez.abilityplus.data.repository

import com.charirodriguez.abilityplus.data.local.dao.PersonaDao
import com.charirodriguez.abilityplus.data.local.entity.PersonaEntity
import kotlinx.coroutines.flow.Flow

class PersonaRepository(
    private val personaDao: PersonaDao
) {
    fun getPersonasActivas(): Flow<List<PersonaEntity>> = personaDao.getActivas()

    suspend fun addPersona(persona: PersonaEntity): Long = personaDao.insert(persona)

    suspend fun updatePersona(persona: PersonaEntity) = personaDao.update(persona)

    suspend fun deletePersona(persona: PersonaEntity) = personaDao.delete(persona)

    suspend fun getPersonaById(id: Long): PersonaEntity? = personaDao.getById(id)
}
