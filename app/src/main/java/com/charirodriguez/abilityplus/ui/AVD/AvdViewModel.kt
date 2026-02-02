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

data class ResultadoAvd(
    val rojos: Int,
    val amarillos: Int,
    val verdes: Int,
    val puntos: Int,
    val grado: Int,
    val etiqueta: String
)

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

    val resultado: StateFlow<ResultadoAvd> =
        combine(actividades, valoraciones) { acts, vals ->
            // Contamos sobre TODAS las actividades del catálogo (11)
            var rojos = 0
            var amarillos = 0
            var verdes = 0
            var puntos = 0

            acts.forEach { act ->
                val semaforo = vals[act.id] ?: 0  // si no hay valoración, lo tratamos como VERDE (0)

                when {
                    semaforo >= 2 -> { // rojo
                        rojos++
                        puntos += 2
                    }
                    semaforo == 1 -> { // amarillo
                        amarillos++
                        puntos += 1
                    }
                    else -> { // verde (0 o null)
                        verdes++
                    }
                }
            }

            // Umbrales MVP (11 actividades => puntos max 22)
            val grado = when {
                puntos >= 16 -> 3
                puntos >= 9 -> 2
                puntos >= 3 -> 1
                else -> 0
            }

            val etiqueta = when (grado) {
                3 -> "Grado 3 (gran dependencia)"
                2 -> "Grado 2 (dependencia severa)"
                1 -> "Grado 1 (dependencia moderada)"
                else -> "Sin grado"
            }

            ResultadoAvd(
                rojos = rojos,
                amarillos = amarillos,
                verdes = verdes,
                puntos = puntos,
                grado = grado,
                etiqueta = etiqueta
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResultadoAvd(0, 0, 0, 0, 0, "Sin grado")
        )

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
