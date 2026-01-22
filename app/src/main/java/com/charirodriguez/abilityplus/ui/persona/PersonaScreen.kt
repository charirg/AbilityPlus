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

@Composable
fun PersonaScreen(viewModel: PersonaViewModel) {
    var mostrandoFormulario by remember { mutableStateOf(false) }
    var personaEnEdicion by remember { mutableStateOf<PersonaEntity?>(null) }

    if (mostrandoFormulario) {
        PersonaForm(
            personaInicial = personaEnEdicion,
            onGuardar = { nombre, apellidos ->
                val editando = personaEnEdicion
                if (editando == null) {
                    viewModel.addPersona(nombre, apellidos)
                } else {
                    viewModel.editarPersona(editando, nombre, apellidos)
                }
                personaEnEdicion = null
                mostrandoFormulario = false
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

@Composable
private fun PersonaForm(
    personaInicial: PersonaEntity?,
    onGuardar: (nombre: String, apellidos: String?) -> Unit,
    onCancelar: () -> Unit
)
{
    var nombre by remember(personaInicial) { mutableStateOf(personaInicial?.nombre ?: "") }
    var apellidos by remember(personaInicial) { mutableStateOf(personaInicial?.apellidos ?: "") }


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
    onEliminar: (PersonaEntity) -> Unit,
    onRestaurar: (PersonaEntity) -> Unit,
    onEditar: (PersonaEntity) -> Unit
)
 {
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
        )   {
            Column {
                Text(text = persona.nombre, style = MaterialTheme.typography.titleMedium)
                persona.apellidos?.let {
                    Text(text = it, style = MaterialTheme.typography.bodyMedium) }
                    }

            if (persona.activo) {
                TextButton(onClick = { onEliminar(persona) }) {
                    Text("Eliminar")
                }
            } else {
                TextButton(onClick = { onRestaurar(persona) }) {
                    Text("Restaurar")
                }
            }

            }
        }
    }




