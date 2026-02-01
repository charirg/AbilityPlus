package com.charirodriguez.abilityplus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.charirodriguez.abilityplus.data.local.entity.seed.ActividadBvdEntity

@Entity(
    tableName = "valoracion_actividad",
    foreignKeys = [
        ForeignKey(
            entity = PersonaEntity::class,
            parentColumns = ["id"],
            childColumns = ["personaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ActividadBvdEntity::class,
            parentColumns = ["id"],
            childColumns = ["actividadId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("personaId"),
        Index("actividadId"),
        Index(value = ["personaId", "actividadId"], unique = true)
    ]
)
data class ValoracionActividadEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personaId: Long,
    val actividadId: Int,
    val semaforo: Int
)
