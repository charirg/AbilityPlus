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

import androidx.sqlite.db.SupportSQLiteDatabase
import com.charirodriguez.abilityplus.data.local.seed.SeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import android.util.Log
import com.charirodriguez.abilityplus.data.local.dao.ValoracionActividadDao
import com.charirodriguez.abilityplus.data.local.entity.ValoracionActividadEntity
import com.charirodriguez.abilityplus.data.local.entity.PersonaDiagnosticoEntity
import com.charirodriguez.abilityplus.data.local.dao.PersonaDiagnosticoDao
import com.charirodriguez.abilityplus.data.local.entity.PersonaCifEntity
import com.charirodriguez.abilityplus.data.local.dao.PersonaCifDao



@Database(
    entities = [
        PersonaEntity::class,
        DiagnosticoCie10Entity::class,
        FuncionalidadCifEntity::class,
        ActividadBvdEntity::class,
        TareaBvdEntity::class,
        ValoracionActividadEntity::class,
        PersonaDiagnosticoEntity::class,
        PersonaCifEntity::class,

    ],
    version = 5,
    exportSchema = false
)
abstract class AbilityPlusDatabase : RoomDatabase() {

    abstract fun personaDao(): PersonaDao
    abstract fun diagnosticoCie10Dao(): DiagnosticoCie10Dao
    abstract fun funcionalidadCifDao(): FuncionalidadCifDao
    abstract fun actividadBvdDao(): ActividadBvdDao
    abstract fun tareaBvdDao(): TareaBvdDao

    abstract fun valoracionActividadDao(): ValoracionActividadDao

    abstract fun PersonaDiagnosticoDao(): PersonaDiagnosticoDao

    abstract fun personaCifDao(): PersonaCifDao




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
                    .addCallback(SEED_CALLBACK)
                    .build()

                INSTANCE = instance
                instance
            }
        }

        private val SEED_CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                Log.d("SEED", "onCreate -> seed start")

                CoroutineScope(Dispatchers.IO).launch {
                    val database = INSTANCE ?: return@launch

                    // CIE-10
                    if (database.diagnosticoCie10Dao().count() == 0) {
                        database.diagnosticoCie10Dao()
                            .insertAll(SeedData.diagnosticosCie10)
                        Log.d("SEED", "Insertados CIE10")
                    }

                    // CIF
                    if (database.funcionalidadCifDao().count() == 0) {
                        database.funcionalidadCifDao()
                            .insertAll(SeedData.funcionalidadesCif)
                        Log.d("SEED", "Insertadas CIF")
                    }

                    // Actividades BVD
                    if (database.actividadBvdDao().count() == 0) {
                        database.actividadBvdDao()
                            .insertAll(SeedData.actividadesBvd)
                        Log.d("SEED", "Insertadas Actividades BVD")
                    }

                    // Tareas BVD
                    if (SeedData.tareasBvd.isNotEmpty() &&
                        database.tareaBvdDao().count() == 0
                    ) {
                        database.tareaBvdDao()
                            .insertAll(SeedData.tareasBvd)
                        Log.d("SEED", "Insertadas Tareas BVD")
                    }

                    Log.d("SEED", "seed end")
                }
            }
        }


    }
}
