package com.charirodriguez.abilityplus.data.local.entity.seed

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tarea_bvd",
    indices = [Index(value = ["actividadId"])]
)
data class TareaBvdEntity(
    @PrimaryKey
    val id: Int,                 // 1..59 (según tu lista)
    val actividadId: Int,         // FK lógica a ActividadBvdEntity.id
    val nombre: String
)
