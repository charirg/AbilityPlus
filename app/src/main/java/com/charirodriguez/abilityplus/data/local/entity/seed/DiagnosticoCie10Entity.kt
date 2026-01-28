package com.charirodriguez.abilityplus.data.local.entity.seed

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diagnostico_cie10")
data class DiagnosticoCie10Entity(
    @PrimaryKey
    val codigo: String,          // ej: "G30"
    val denominacion: String     // ej: "Enfermedad de Alzheimer"
)
