package com.charirodriguez.abilityplus.ui.persona

import androidx.compose.runtime.*
import com.charirodriguez.abilityplus.data.local.entity.PersonaEntity

@Composable
fun PersonaFormScreen(
    viewModel: PersonaViewModel,
    personaId: Long?,                 // null = crear
    onGuardado: () -> Unit,           // volver a expedientes
    onCancelar: () -> Unit
) {
    val personas by viewModel.personasActivas.collectAsState()
    val personaInicial: PersonaEntity? = personas.firstOrNull { it.id == personaId }

    val errorMensaje by viewModel.errorMensaje.collectAsState()

    PersonaForm(
        personaInicial = personaInicial,
        errorMensaje = errorMensaje,
        onGuardar = { numeroExpediente, sexo, edadValoracion, fechaNacimientoMillis ->
            if (personaInicial == null) {
                viewModel.addPersona(numeroExpediente, sexo, edadValoracion, fechaNacimientoMillis)
            } else {
                viewModel.editarPersona(personaInicial, numeroExpediente, sexo, edadValoracion, fechaNacimientoMillis)
            }
            onGuardado()
        },
        onCancelar = onCancelar
    )
}
