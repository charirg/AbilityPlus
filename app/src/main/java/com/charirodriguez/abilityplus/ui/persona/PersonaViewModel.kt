package com.charirodriguez.abilityplus.ui.persona

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.charirodriguez.abilityplus.data.local.entity.PersonaEntity
import com.charirodriguez.abilityplus.data.repository.PersonaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine


class PersonaViewModel(
    private val repo: PersonaRepository
) : ViewModel() {

    val personasActivas: StateFlow<List<PersonaEntity>> =
        repo.getPersonasActivas()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addPersona(nombre: String, apellidos: String?) {
        viewModelScope.launch {
            repo.addPersona(
                PersonaEntity(
                    nombre = nombre.trim(),
                    apellidos = apellidos?.trim()?.takeIf { it.isNotBlank() }
                )
            )
        }
    }

    fun eliminarPersona(persona: PersonaEntity) {
        viewModelScope.launch {
            repo.updatePersona(persona.copy(activo = false))
        }
    }

    private val _mostrarEliminadas = MutableStateFlow(false)
    val mostrarEliminadas = _mostrarEliminadas.asStateFlow()

    fun setMostrarEliminadas(valor: Boolean) {
        _mostrarEliminadas.value = valor
    }

    val personasMostradas: StateFlow<List<PersonaEntity>> =
        combine(
            repo.getPersonasActivas(),
            repo.getPersonasEliminadas(),
            mostrarEliminadas
        ) { activas, eliminadas, mostrar ->
            if (mostrar) eliminadas else activas
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun restaurarPersona(persona: PersonaEntity) {
        viewModelScope.launch {
            repo.updatePersona(persona.copy(activo = true))
        }
    }
    fun editarPersona(persona: PersonaEntity, nombre: String, apellidos: String?) {
        viewModelScope.launch {
            repo.updatePersona(
                persona.copy(
                    nombre = nombre.trim(),
                    apellidos = apellidos?.trim()?.takeIf { it.isNotBlank() }
                )
            )
        }
    }



}

class PersonaViewModelFactory(
    private val repo: PersonaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PersonaViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
