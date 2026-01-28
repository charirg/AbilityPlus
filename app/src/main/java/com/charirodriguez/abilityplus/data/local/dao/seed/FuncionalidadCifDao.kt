package com.charirodriguez.abilityplus.data.local.dao.seed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.charirodriguez.abilityplus.data.local.entity.seed.FuncionalidadCifEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FuncionalidadCifDao {

    @Query("SELECT * FROM funcionalidad_cif ORDER BY codigo ASC")
    fun getAll(): Flow<List<FuncionalidadCifEntity>>

    @Query("SELECT COUNT(*) FROM funcionalidad_cif")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FuncionalidadCifEntity>)
}
