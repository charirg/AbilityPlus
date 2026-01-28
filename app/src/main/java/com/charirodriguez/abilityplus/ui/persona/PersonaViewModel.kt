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
    private val _errorMensaje = MutableStateFlow<String?>(null)
    val errorMensaje = _errorMensaje.asStateFlow()

    private fun esFechaValida(texto: String): Boolean {
        return try {
            java.time.LocalDate.parse(texto)
            true
        } catch (e: Exception) {
            false
        }
    }






    fun addPersona(
        numeroExpediente: String,
        sexo: String?,
        edadValoracion: Int?,
        fechaNacimientoMillis: Long?
    ) {
        viewModelScope.launch {

            if (numeroExpediente.isBlank()) {
                _errorMensaje.value = "El número de expediente es obligatorio"
                return@launch
            }

            _errorMensaje.value = null

            repo.addPersona(
                PersonaEntity(
                    numeroExpediente = numeroExpediente.trim(),
                    sexo = sexo,
                    edadValoracion = edadValoracion,
                    fechaNacimientoMillis = fechaNacimientoMillis
                )
            )
            if (fechaNacimientoMillis == null && numeroExpediente.isNotBlank()) {
                // Si el usuario escribió algo en fecha pero es inválido
            }

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
    fun editarPersona(
        persona: PersonaEntity,
        numeroExpediente: String,
        sexo: String?,
        edadValoracion: Int?,
        fechaNacimientoMillis: Long?
    ) {
        viewModelScope.launch {if (numeroExpediente.isBlank()) {
            _errorMensaje.value = "El número de expediente es obligatorio"
            return@launch
        }

            _errorMensaje.value = null

            repo.updatePersona(
                persona.copy(
                    numeroExpediente = numeroExpediente.trim(),
                    sexo = sexo,
                    edadValoracion = edadValoracion,
                    fechaNacimientoMillis = fechaNacimientoMillis
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
