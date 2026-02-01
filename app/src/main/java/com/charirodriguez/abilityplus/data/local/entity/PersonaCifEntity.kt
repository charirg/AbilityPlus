package com.charirodriguez.abilityplus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "persona_cif",
    primaryKeys = ["personaId", "cifCodigo"],
    foreignKeys = [
        ForeignKey(
            entity = PersonaEntity::class,
            parentColumns = ["id"],
            childColumns = ["personaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("personaId"),
        Index("cifCodigo")
    ]
)
data class PersonaCifEntity(
    val personaId: Long,
    val cifCodigo: String
)
