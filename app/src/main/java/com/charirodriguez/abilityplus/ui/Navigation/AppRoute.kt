package com.charirodriguez.abilityplus.ui.navigation

sealed class AppRoute {
    data object Login : AppRoute()
    data object PersonaList : AppRoute()
    data class PersonaForm(val personaId: Long?) : AppRoute() // null = crear
    data class PerfilFuncional(val personaId: Long) : AppRoute()
}

