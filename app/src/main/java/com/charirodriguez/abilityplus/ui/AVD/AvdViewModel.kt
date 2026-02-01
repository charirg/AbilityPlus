package com.charirodriguez.abilityplus.ui.avd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.charirodriguez.abilityplus.data.local.dao.ValoracionActividadDao
import com.charirodriguez.abilityplus.data.local.dao.seed.ActividadBvdDao
import com.charirodriguez.abilityplus.data.local.entity.ValoracionActividadEntity
import com.charirodriguez.abilityplus.data.local.entity.seed.ActividadBvdEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AvdViewModel(
    private val personaId: Long,
    private val actividadDao: ActividadBvdDao,
    private val valoracionDao: ValoracionActividadDao
) : ViewModel() {

    val actividades: StateFlow<List<ActividadBvdEntity>> =
        actividadDao.getAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val valoraciones: StateFlow<Map<Int, Int>> =
        valoracionDao.getByPersona(personaId)
            .map { list -> list.associate { it.actividadId to it.semaforo } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    fun setSemaforo(actividadId: Int, semaforo: Int) {
        viewModelScope.launch {
            valoracionDao.upsert(
                ValoracionActividadEntity(
                    personaId = personaId,
                    actividadId = actividadId,
                    semaforo = semaforo
                )
            )
        }
    }
}

class AvdViewModelFactory(
    private val personaId: Long,
    private val actividadDao: ActividadBvdDao,
    private val valoracionDao: ValoracionActividadDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AvdViewModel(personaId, actividadDao, valoracionDao) as T
    }
}
