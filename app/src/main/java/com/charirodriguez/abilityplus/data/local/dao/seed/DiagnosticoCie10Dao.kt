

package com.charirodriguez.abilityplus.data.local.dao.seed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.charirodriguez.abilityplus.data.local.entity.seed.DiagnosticoCie10Entity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosticoCie10Dao {

    @Query("SELECT * FROM diagnostico_cie10 ORDER BY codigo ASC")
    fun getAll(): Flow<List<DiagnosticoCie10Entity>>

    @Query("SELECT COUNT(*) FROM diagnostico_cie10")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<DiagnosticoCie10Entity>)
}
