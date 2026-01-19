package com.charirodriguez.abilityplus.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.charirodriguez.abilityplus.data.local.dao.PersonaDao
import com.charirodriguez.abilityplus.data.local.entity.PersonaEntity

@Database(
    entities = [PersonaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AbilityPlusDatabase : RoomDatabase() {

    abstract fun personaDao(): PersonaDao

    companion object {
        @Volatile
        private var INSTANCE: AbilityPlusDatabase? = null

        fun getInstance(context: Context): AbilityPlusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AbilityPlusDatabase::class.java,
                    "abilityplus.db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
