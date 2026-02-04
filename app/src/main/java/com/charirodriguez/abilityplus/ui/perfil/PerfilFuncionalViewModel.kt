package com.charirodriguez.abilityplus.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.charirodriguez.abilityplus.data.local.dao.PersonaDao
import com.charirodriguez.abilityplus.data.local.dao.PersonaDiagnosticoDao
import com.charirodriguez.abilityplus.data.local.entity.PersonaDiagnosticoEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerfilFuncionalViewModel(
private val personaId: Long,
private val personaDiagnosticoDao: PersonaDiagnosticoDao,
private val personaDao: PersonaDao
) : ViewModel() {

    fun asegurarFechaValoracion() {
        viewModelScope.launch {
            val persona = personaDao.getById(personaId) ?: return@launch

            if (persona.fechaValoracionMillis == null) {
                personaDao.update(
                    persona.copy(
                        fechaValoracionMillis = System.currentTimeMillis()
                    )
                )
            }
        }
    }


    // aquí exponemos SOLO el código (ej "G30") o null
    val cieSeleccionado: StateFlow<String?> =
        personaDiagnosticoDao.getByPersona(personaId)
            .map { it?.cieCodigo }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    fun seleccionarCie(codigo: String) {
        viewModelScope.launch {
            personaDiagnosticoDao.upsert(
                PersonaDiagnosticoEntity(
                    personaId = personaId,
                    cieCodigo = codigo
                )
            )
        }
    }

}

class PerfilFuncionalViewModelFactory(
    private val personaId: Long,
    private val dao: PersonaDiagnosticoDao,
    private val personaDao: PersonaDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PerfilFuncionalViewModel(
            personaId,
            dao,
            personaDao
        ) as T
    }
}

