package com.charirodriguez.abilityplus.data.local.entity.seed

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actividad_bvd")
data class ActividadBvdEntity(
    @PrimaryKey
    val id: Int,                 // 1..11 (según tu lista)
    val nombre: String
)
