package com.charirodriguez.abilityplus.ui.persona

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.charirodriguez.abilityplus.data.local.entity.PersonaEntity
import java.time.LocalDate
import java.time.ZoneId
import java.time.Instant
import java.time.format.DateTimeFormatter



@Composable
fun PersonaScreen(viewModel: PersonaViewModel) {
    val errorMensaje by viewModel.errorMensaje.collectAsState()
    var mostrandoFormulario by remember { mutableStateOf(false) }
    var personaEnEdicion by remember { mutableStateOf<PersonaEntity?>(null) }

    if (mostrandoFormulario) {
        PersonaForm(
            personaInicial = personaEnEdicion,
            errorMensaje = errorMensaje,
            onGuardar = { numeroExpediente, sexo, edadValoracion, fechaNacimientoMillis ->
                val editando = personaEnEdicion
                if (editando == null) {
                    viewModel.addPersona(numeroExpediente, sexo, edadValoracion, fechaNacimientoMillis)
                } else {
                    viewModel.editarPersona(
                        editando,
                        numeroExpediente,
                        sexo,
                        edadValoracion,
                        fechaNacimientoMillis
                    )
                }
                if (numeroExpediente.isNotBlank()) {
                    personaEnEdicion = null
                    mostrandoFormulario = false
                }

            },
            onCancelar = {
                personaEnEdicion = null
                mostrandoFormulario = false
            }
        )

    } else {
        PersonaList(
            viewModel = viewModel,
            onNuevaPersona = {
                personaEnEdicion = null
                mostrandoFormulario = true
            },
            onEditar = { persona ->
                personaEnEdicion = persona
                mostrandoFormulario = true
            }
        )
    }
}

@Composable
private fun PersonaList(
    viewModel: PersonaViewModel,
    onNuevaPersona: () -> Unit,
    onEditar: (PersonaEntity) -> Unit
)

{
    val personas by viewModel.personasMostradas.collectAsState()
    val mostrarEliminadas by viewModel.mostrarEliminadas.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onNuevaPersona) {
            Text("Nueva persona")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Mostrar eliminadas")
            Spacer(modifier = Modifier.width(12.dp))
            Switch(
                checked = mostrarEliminadas,
                onCheckedChange = { viewModel.setMostrarEliminadas(it) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))


        LazyColumn {
            items(personas) { persona ->
                PersonaItem(
                    persona = persona,
                    onEliminar = { viewModel.eliminarPersona(it) },
                    onRestaurar = { viewModel.restaurarPersona(it) },
                    onEditar = { onEditar(it) }
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonaForm(
    personaInicial: PersonaEntity?,
    errorMensaje: String?,
    onGuardar: (numeroExpediente: String, sexo: String?, edadValoracion: Int?, fechaNacimientoMillis: Long?) -> Unit,
    onCancelar: () -> Unit
) {
    var numeroExpediente by remember(personaInicial) {
        mutableStateOf(personaInicial?.numeroExpediente ?: "")
    }
    var sexo by remember(personaInicial) { mutableStateOf(personaInicial?.sexo) }
    var edadValoracionText by remember(personaInicial) {
        mutableStateOf(personaInicial?.edadValoracion?.toString() ?: "")
    }
    var fechaNacimientoText by remember(personaInicial) {
        mutableStateOf(
            personaInicial?.fechaNacimientoMillis?.let { millis ->
                java.time.Instant.ofEpochMilli(millis)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate()
                    .toString() // "YYYY-MM-DD"
            } ?: ""
        )
    }

    var errorFecha by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = if (personaInicial == null) "Nueva persona" else "Editar persona",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = numeroExpediente,
            onValueChange = { numeroExpediente = it },
            label = { Text("Número de expediente") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        var sexoExpanded by remember { mutableStateOf(false) }
        val opcionesSexo = listOf("M", "F", "X")

        ExposedDropdownMenuBox(
            expanded = sexoExpanded,
            onExpandedChange = { sexoExpanded = !sexoExpanded }
        ) {
            OutlinedTextField(
                value = sexo ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Sexo") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexoExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = sexoExpanded,
                onDismissRequest = { sexoExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Sin especificar") },
                    onClick = {
                        sexo = null
                        sexoExpanded = false
                    }
                )
                opcionesSexo.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            sexo = opcion
                            sexoExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = edadValoracionText,
            onValueChange = { nuevo ->
                edadValoracionText = nuevo.filter { it.isDigit() }.take(3)
            },
            label = { Text("Edad (valoración)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = fechaNacimientoText,
            onValueChange = { fechaNacimientoText = it },
            label = { Text("Fecha nacimiento (YYYY-MM-DD)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()

        )

        errorFecha?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        errorMensaje?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    val edadValoracion = edadValoracionText.toIntOrNull()
                    val fechaMillis = if (fechaNacimientoText.isBlank()) {
                        personaInicial?.fechaNacimientoMillis

                    } else {
                        try {
                            java.time.LocalDate.parse(fechaNacimientoText)
                                .atStartOfDay(java.time.ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                        } catch (e: Exception) {
                            errorFecha = "Fecha inválida. Usa YYYY-MM-DD"
                            return@Button
                        }
                    }

                    errorFecha = null
                    onGuardar(numeroExpediente.trim(), sexo, edadValoracion, fechaMillis)
                },

                        enabled = true
            ) {
                Text("Guardar")
            }

            OutlinedButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    }
}


@Composable
private fun PersonaItem(
    persona: PersonaEntity,
    onEliminar: (PersonaEntity) -> Unit,
    onRestaurar: (PersonaEntity) -> Unit,
    onEditar: (PersonaEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onEditar(persona) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = persona.numeroExpediente,
                    style = MaterialTheme.typography.titleMedium
                )
                persona.sexo?.let {
                    Text(
                        text = "Sexo: $it",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                persona.edadValoracion?.let {
                    Text(
                        text = "Edad: $it",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                persona.fechaNacimientoMillis?.let { millis ->
                    val fecha = Instant.ofEpochMilli(millis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                    Text(
                        text = "Nacimiento: ${
                            fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        }",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }


            }


            if (persona.activo) {
                TextButton(onClick = { onEliminar(persona) }) { Text("Eliminar") }
            } else {
                TextButton(onClick = { onRestaurar(persona) }) { Text("Restaurar") }
            }
        }
    }
}






