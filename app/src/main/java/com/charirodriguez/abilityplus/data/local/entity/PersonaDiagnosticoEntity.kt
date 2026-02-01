package com.charirodriguez.abilityplus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "persona_diagnostico",
    foreignKeys = [
        ForeignKey(
            entity = PersonaEntity::class,
            parentColumns = ["id"],
            childColumns = ["personaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("personaId")]
)
data class PersonaDiagnosticoEntity(
    @PrimaryKey val personaId: Long,
    val cieCodigo: String
)
