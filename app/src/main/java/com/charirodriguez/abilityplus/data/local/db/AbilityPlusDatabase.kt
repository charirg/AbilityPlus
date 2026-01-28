package com.charirodriguez.abilityplus.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.charirodriguez.abilityplus.data.local.dao.PersonaDao
import com.charirodriguez.abilityplus.data.local.entity.PersonaEntity

import com.charirodriguez.abilityplus.data.local.dao.seed.ActividadBvdDao
import com.charirodriguez.abilityplus.data.local.dao.seed.DiagnosticoCie10Dao
import com.charirodriguez.abilityplus.data.local.dao.seed.FuncionalidadCifDao
import com.charirodriguez.abilityplus.data.local.dao.seed.TareaBvdDao

import com.charirodriguez.abilityplus.data.local.entity.seed.ActividadBvdEntity
import com.charirodriguez.abilityplus.data.local.entity.seed.DiagnosticoCie10Entity
import com.charirodriguez.abilityplus.data.local.entity.seed.FuncionalidadCifEntity
import com.charirodriguez.abilityplus.data.local.entity.seed.TareaBvdEntity

@Database(
    entities = [
        PersonaEntity::class,
        DiagnosticoCie10Entity::class,
        FuncionalidadCifEntity::class,
        ActividadBvdEntity::class,
        TareaBvdEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AbilityPlusDatabase : RoomDatabase() {

    abstract fun personaDao(): PersonaDao
    abstract fun diagnosticoCie10Dao(): DiagnosticoCie10Dao
    abstract fun funcionalidadCifDao(): FuncionalidadCifDao
    abstract fun actividadBvdDao(): ActividadBvdDao
    abstract fun tareaBvdDao(): TareaBvdDao


    companion object {
        @Volatile
        private var INSTANCE: AbilityPlusDatabase? = null

        fun getInstance(context: Context): AbilityPlusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(

                    context.applicationContext,
                    AbilityPlusDatabase::class.java,
                    "abilityplus.db"
                )
                    .fallbackToDestructiveMigration()

                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
