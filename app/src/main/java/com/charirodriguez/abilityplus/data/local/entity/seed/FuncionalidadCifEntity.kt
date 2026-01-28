package com.charirodriguez.abilityplus.data.local.entity.seed

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "funcionalidad_cif")
data class FuncionalidadCifEntity(
    @PrimaryKey
    val codigo: String,          // ej: "b144"
    val denominacion: String     // ej: "Memoria"
)
