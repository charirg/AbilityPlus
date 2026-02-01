package com.charirodriguez.abilityplus.ui.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.charirodriguez.abilityplus.data.local.entity.seed.ActividadBvdEntity
import com.charirodriguez.abilityplus.data.local.entity.seed.DiagnosticoCie10Entity
import com.charirodriguez.abilityplus.data.local.entity.seed.FuncionalidadCifEntity
import com.charirodriguez.abilityplus.ui.avd.AvdScreen
import com.charirodriguez.abilityplus.ui.cif.FuncionalidadCifScreen
import com.charirodriguez.abilityplus.ui.diagnostico.DiagnosticosScreen



    @OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilFuncionalScreen(
    personaId: Long,
    diagnosticos: List<DiagnosticoCie10Entity>,
    cifs: List<FuncionalidadCifEntity>,
    actividades: List<ActividadBvdEntity>,
    valoraciones: Map<Int, Int>,
    cieSeleccionado: String?,
    onSeleccionarCie: (String) -> Unit,
    onCambiarSemaforo: (Int, Int) -> Unit,
    cifsSeleccionadas: Set<String>,
    onToggleCif: (String) -> Unit,
    cifError: String?,
    onVolverDatosPersonales: () -> Unit,
    onFinalizar: () -> Unit
) {

    Scaffold(
        topBar = { TopAppBar(title = { Text("Perfil funcional") }) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                OutlinedButton(onClick = onVolverDatosPersonales) {
                    Text("Volver a datos personales")
                }

            }

            item {
                Text("Expediente: (pendiente)  •  personaId=$personaId") }
            item {
                val estado = if (cieSeleccionado == null) {
                    "Perfil incompleto: falta diagnóstico"
                } else {
                    "Perfil básico completo"
                }

                Text(
                    text = estado,
                    color = if (cieSeleccionado == null)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                )
            }

            // ---- CIE ----
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Diagnóstico (CIE)", style = MaterialTheme.typography.titleMedium)

                        Box(modifier = Modifier.height(180.dp)) {
                            DiagnosticosScreen(
                                diagnosticos = diagnosticos,
                                seleccionadoCodigo = cieSeleccionado,
                                onSeleccionar = { diag ->
                                    onSeleccionarCie(diag.codigo)
                                }
                            )
                        }
                    }
                }
            }

            // ---- CIF ----
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Funcionalidad (CIF)", style = MaterialTheme.typography.titleMedium)

                        if (cieSeleccionado == null) {
                            Text("Selecciona primero un diagnóstico (CIE) para continuar.")
                        } else {
                            Box(modifier = Modifier.height(180.dp)) {
                                FuncionalidadCifScreen(
                                    cifs = cifs,
                                    seleccionadas = cifsSeleccionadas,
                                    onSeleccionar = { cif -> onToggleCif(cif.codigo) }
                                )
                                if (cifError != null) {
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        text = cifError!!,
                                        color = MaterialTheme.colorScheme.error
                                    )

                                }


                            }
                        }
                    }
                }
            }

            // ---- AVD ----
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Actividades (AVD)", style = MaterialTheme.typography.titleMedium)

                        Box(modifier = Modifier.height(260.dp)) {
                            AvdScreen(
                                actividades = actividades,
                                valoraciones = valoraciones,
                                onCambiarSemaforo = onCambiarSemaforo
                            )
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = onFinalizar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar perfil funcional")
                }

            }
        }
    }
}



