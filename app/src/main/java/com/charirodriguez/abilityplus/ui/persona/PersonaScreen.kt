package com.charirodriguez.abilityplus.ui.persona

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.charirodriguez.abilityplus.data.local.entity.PersonaEntity

@Composable
fun PersonaScreen(viewModel: PersonaViewModel) {
    var mostrandoFormulario by remember { mutableStateOf(false) }

    if (mostrandoFormulario) {
        PersonaForm(
            onGuardar = { nombre, apellidos ->
                viewModel.addPersona(nombre, apellidos)
                mostrandoFormulario = false
            },
            onCancelar = { mostrandoFormulario = false }
        )
    } else {
        PersonaList(
            viewModel = viewModel,
            onNuevaPersona = { mostrandoFormulario = true }
        )
    }
}

@Composable
private fun PersonaList(
    viewModel: PersonaViewModel,
    onNuevaPersona: () -> Unit
) {
    val personas by viewModel.personasActivas.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onNuevaPersona) {
            Text("Nueva persona")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(personas) { persona ->
                PersonaItem(
                    persona = persona,
                    onEliminar = { viewModel.eliminarPersona(it) }
                )
            }
        }
    }
}

@Composable
private fun PersonaForm(
    onGuardar: (nombre: String, apellidos: String?) -> Unit,
    onCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Nueva persona", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { onGuardar(nombre, apellidos) },
                enabled = nombre.isNotBlank()
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
    onEliminar: (PersonaEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = persona.nombre, style = MaterialTheme.typography.titleMedium)
                persona.apellidos?.let {
                    Text(text = it, style = MaterialTheme.typography.bodyMedium)
                }
            }

            TextButton(onClick = { onEliminar(persona) }) {
                Text("Eliminar")
            }
        }
    }
}


