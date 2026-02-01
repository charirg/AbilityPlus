package com.charirodriguez.abilityplus.ui.avd

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.charirodriguez.abilityplus.data.local.entity.seed.ActividadBvdEntity

@Composable
fun AvdScreen(
    actividades: List<ActividadBvdEntity>,
    valoraciones: Map<Int, Int>,
    onCambiarSemaforo: (actividadId: Int, semaforo: Int) -> Unit
) {
    LazyColumn {
        items(actividades) { actividad ->
            val estado = valoraciones[actividad.id] ?: 0

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {

                    Text(actividad.nombre)

                    Spacer(Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SemaforoButton("🟢", estado == 0) {
                            onCambiarSemaforo(actividad.id, 0)
                        }
                        SemaforoButton("🟡", estado == 1) {
                            onCambiarSemaforo(actividad.id, 1)
                        }
                        SemaforoButton("🔴", estado == 2) {
                            onCambiarSemaforo(actividad.id, 2)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SemaforoButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = if (selected)
            ButtonDefaults.buttonColors()
        else
            ButtonDefaults.outlinedButtonColors()
    ) {
        Text(text)
    }
}
