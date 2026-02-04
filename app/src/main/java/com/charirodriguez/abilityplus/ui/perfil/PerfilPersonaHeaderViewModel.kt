package com.charirodriguez.abilityplus.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.charirodriguez.abilityplus.data.local.dao.PersonaDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class PerfilPersonaHeaderViewModel(
    private val personaId: Long,
    private val personaDao: PersonaDao
) : ViewModel() {

    val persona = flow {
        emit(personaDao.getById(personaId))
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )
}
class PerfilPersonaHeaderViewModelFactory(
    private val personaId: Long,
    private val personaDao: PersonaDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PerfilPersonaHeaderViewModel(personaId, personaDao) as T
    }
}
