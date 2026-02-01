package com.charirodriguez.abilityplus.ui.diagnostico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.charirodriguez.abilityplus.data.local.dao.seed.DiagnosticoCie10Dao
import com.charirodriguez.abilityplus.data.local.entity.seed.DiagnosticoCie10Entity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DiagnosticoViewModel(
    diagnosticoDao: DiagnosticoCie10Dao
) : ViewModel() {

    val diagnosticos: StateFlow<List<DiagnosticoCie10Entity>> =
        diagnosticoDao.getAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}

class DiagnosticoViewModelFactory(
    private val diagnosticoDao: DiagnosticoCie10Dao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DiagnosticoViewModel(diagnosticoDao) as T
    }
}
