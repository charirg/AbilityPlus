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
import com.charirodriguez.abilityplus.ui.avd.ResultadoAvd



    @OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilFuncionalScreen(
    personaId: Long,
    numeroExpediente: String?,
    fechaValoracionMillis: Long?,
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
    resultadoAvd: com.charirodriguez.abilityplus.ui.avd.ResultadoAvd,
    onFinalizar: () -> Unit,
    onGenerarInforme: () -> Unit,


    ) {

    Scaffold(
        topBar = { TopAppBar(title = { Text("Perfil funcional") }) }
    ) { padding ->
        val esPerfilMental = (cieSeleccionado == "G30") // MVP: Alzheimer = perfil mental

        val valoradas = valoraciones.keys.toSet()
        val valoradasSin11 = valoradas.count { it != 11 }
        val valoradasTotal = valoradas.size

        val avdOk = if (esPerfilMental) {
            valoradasTotal >= 11
        } else {
            valoradasSin11 >= 10
        }

        val puedeGenerarPdf =
            cieSeleccionado != null &&
                    cifsSeleccionadas.isNotEmpty() &&
                    avdOk

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            //item {
                //OutlinedButton(onClick = onVolverDatosPersonales) { Text("Ir a datos personales") } }

            item {
                Text("Expediente: ${numeroExpediente ?: ""}")
                val fechaTxt = fechaValoracionMillis?.let { millis ->
                    java.time.Instant.ofEpochMilli(millis)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate()
                        .toString()
                } ?: "—"

                Text("Fecha de valoración: $fechaTxt")}

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
               // ---- RESULTADO ----
            item{
                Card(modifier = Modifier.fillMaxWidth()) {
                   Column(modifier = Modifier.padding(12.dp)) {
                       Text(
                           text = "Resultado de la valoración:",
                           style = MaterialTheme.typography.titleMedium
                       )
                       Spacer(Modifier.height(6.dp))

                       Text(
                           text = resultadoAvd.etiqueta,
                           style = MaterialTheme.typography.titleLarge
                       )
                       Spacer(Modifier.height(6.dp))
                       Text("- Actividades con desempeño activo: ${resultadoAvd.verdes}")
                       Text("- Actividades con desempeño asistido: ${resultadoAvd.amarillos}")
                       Text("- Actividades con desempeño pasivo: ${resultadoAvd.rojos}")

                   }
               }

           }
            item {
                Button(
                    onClick = onFinalizar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar/Salir")
                }

            }

            item {
                if (!puedeGenerarPdf) {
                    Text(
                        text = "Completa: CIE (1), CIF (≥1) y AVD (${if (esPerfilMental) "11/11" else "10/10"}).",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }

            item {
                Button(
                    onClick = onGenerarInforme,
                    enabled = puedeGenerarPdf,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Generar informe PDF")
                }
            }





        }
    }
}



