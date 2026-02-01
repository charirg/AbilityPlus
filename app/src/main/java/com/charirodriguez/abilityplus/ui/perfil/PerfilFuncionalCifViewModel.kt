package com.charirodriguez.abilityplus.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.charirodriguez.abilityplus.data.local.dao.PersonaCifDao
import com.charirodriguez.abilityplus.data.local.entity.PersonaCifEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerfilFuncionalCifViewModel(
    private val personaId: Long,
    private val dao: PersonaCifDao

) : ViewModel() {
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()



    // Set de códigos CIF seleccionados para esta persona
    val cifsSeleccionadas: StateFlow<Set<String>> =
        dao.getByPersona(personaId)
            .map { list -> list.map { it.cifCodigo }.toSet() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet()
            )

    fun toggleCif(codigo: String) {
        viewModelScope.launch {
            val actuales = cifsSeleccionadas.value

            if (actuales.contains(codigo)) {
                // Si ya estaba marcada, permitimos desmarcar siempre
                dao.delete(personaId, codigo)
                _error.value = null
                return@launch
            }

            // Si no estaba marcada, comprobamos límite
            if (actuales.size >= 5) {
                _error.value = "Máximo 5 funcionalidades CIF."
                return@launch
            }

            dao.insert(PersonaCifEntity(personaId = personaId, cifCodigo = codigo))
            _error.value = null
        }
    }
}

class PerfilFuncionalCifViewModelFactory(
    private val personaId: Long,
    private val dao: PersonaCifDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PerfilFuncionalCifViewModel(personaId, dao) as T
    }
}
