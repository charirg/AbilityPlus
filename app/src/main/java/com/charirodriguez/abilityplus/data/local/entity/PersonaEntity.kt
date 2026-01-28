
package com.charirodriguez.abilityplus.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "persona",
    indices = [
        Index(value = ["numeroExpediente"], unique = true)
    ]
)
data class PersonaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    // Identificador de negocio (lo introduces tú o viene de otro sistema, NO se autogenera)
    val numeroExpediente: String,

    // Fecha en epoch millis (opcional)
    val fechaNacimientoMillis: Long? = null,

    // Edad “foto fija” de valoración (opcional)
    val edadValoracion: Int? = null,

    // "M", "F", "X" (opcional por ahora)
    val sexo: String? = null,

    // Borrado lógico
    val activo: Boolean = true
)
