package com.charirodriguez.abilityplus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persona")
data class PersonaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val nombre: String,
    val apellidos: String? = null,
    val fechaNacimientoMillis: Long? = null,
    val activo: Boolean = true
)
