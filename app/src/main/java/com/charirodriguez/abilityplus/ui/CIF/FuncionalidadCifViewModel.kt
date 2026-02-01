package com.charirodriguez.abilityplus.ui.cif

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.charirodriguez.abilityplus.data.local.dao.seed.FuncionalidadCifDao
import com.charirodriguez.abilityplus.data.local.entity.seed.FuncionalidadCifEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FuncionalidadCifViewModel(
    cifDao: FuncionalidadCifDao
) : ViewModel() {

    val cifs: StateFlow<List<FuncionalidadCifEntity>> =
        cifDao.getAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}

class FuncionalidadCifViewModelFactory(
    private val cifDao: FuncionalidadCifDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FuncionalidadCifViewModel(cifDao) as T
    }
}
