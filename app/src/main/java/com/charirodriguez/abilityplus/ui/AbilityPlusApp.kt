package com.charirodriguez.abilityplus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.charirodriguez.abilityplus.data.local.db.AbilityPlusDatabase
import com.charirodriguez.abilityplus.data.repository.PersonaRepository
import com.charirodriguez.abilityplus.ui.navigation.AppRoute
import com.charirodriguez.abilityplus.ui.perfil.PerfilFuncionalScreen
import com.charirodriguez.abilityplus.ui.persona.PersonaFormScreen
import com.charirodriguez.abilityplus.ui.persona.PersonaViewModel
import com.charirodriguez.abilityplus.ui.persona.PersonaViewModelFactory
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbilityPlusApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val db = com.charirodriguez.abilityplus.data.local.db.AbilityPlusDatabase.getInstance(context)
    val diagnosticoDao = db.diagnosticoCie10Dao()

    val diagnosticoViewModel: com.charirodriguez.abilityplus.ui.diagnostico.DiagnosticoViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel(
            factory = com.charirodriguez.abilityplus.ui.diagnostico.DiagnosticoViewModelFactory(diagnosticoDao)
        )

    val diagnosticos by diagnosticoViewModel.diagnosticos.collectAsState()

    val cifDao = db.funcionalidadCifDao()

    val cifViewModel: com.charirodriguez.abilityplus.ui.cif.FuncionalidadCifViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel(
            factory = com.charirodriguez.abilityplus.ui.cif.FuncionalidadCifViewModelFactory(cifDao)
        )

    val cifs by cifViewModel.cifs.collectAsState()


    var route by remember { mutableStateOf<AppRoute>(AppRoute.Login) }

    when (val r = route) {

        is AppRoute.Login -> {
            Scaffold { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = { route = AppRoute.PersonaList }) {
                        Text("Entrar")
                    }
                }
            }
        }

        is AppRoute.PersonaList -> {
            val context = LocalContext.current

            val db = remember { AbilityPlusDatabase.getInstance(context) }
            val personaDao = remember { db.personaDao() }
            val repo = remember { PersonaRepository(personaDao) }

            val personaViewModel: PersonaViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = PersonaViewModelFactory(repo)
                )

            val personas by personaViewModel.personasMostradas.collectAsState()
            val mostrarEliminadas by personaViewModel.mostrarEliminadas.collectAsState()

            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("Expedientes") })
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        route = AppRoute.PersonaForm(personaId = null)
                    }) {
                        Text("+")
                    }
                }
            ) { padding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Button(onClick = { route = AppRoute.Login }) {
                        Text("Salir")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Mostrar eliminadas")
                        Spacer(Modifier.width(12.dp))
                        Switch(
                            checked = mostrarEliminadas,
                            onCheckedChange = { personaViewModel.setMostrarEliminadas(it) }
                        )
                    }


                    if (personas.isEmpty()) {
                        Text("No hay expedientes")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(personas) { persona ->
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "Expediente: ${persona.numeroExpediente}",
                                            style = MaterialTheme.typography.titleMedium
                                        )

                                        Spacer(Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {

                                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                                OutlinedButton(
                                                    onClick = {
                                                        route =
                                                            AppRoute.PersonaForm(personaId = persona.id)
                                                    }
                                                ) {
                                                    Text("Datos personales")
                                                }

                                                Button(
                                                    onClick = {
                                                        route =
                                                            AppRoute.PerfilFuncional(personaId = persona.id)
                                                    }
                                                ) {
                                                    Text("Perfil funcional")
                                                }
                                            }
                                            if (persona.activo) {
                                                IconButton(onClick = {
                                                    personaViewModel.eliminarPersona(
                                                        persona
                                                    )
                                                }) {
                                                    Icon(
                                                        imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                                                        contentDescription = "Eliminar"
                                                    )
                                                }
                                            } else {
                                                IconButton(onClick = {
                                                    personaViewModel.restaurarPersona(
                                                        persona
                                                    )
                                                }) {
                                                    Icon(
                                                        imageVector = androidx.compose.material.icons.Icons.Default.Refresh,
                                                        contentDescription = "Restaurar"
                                                    )
                                                }
                                            }


                                        }


                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        is AppRoute.PersonaForm -> {
            val context = LocalContext.current

            val db = remember { AbilityPlusDatabase.getInstance(context) }
            val personaDao = remember { db.personaDao() }
            val repo = remember { PersonaRepository(personaDao) }

            val personaViewModel: PersonaViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = PersonaViewModelFactory(repo)
                )

            PersonaFormScreen(
                viewModel = personaViewModel,
                personaId = r.personaId,
                onGuardado = { route = AppRoute.PersonaList },
                onCancelar = { route = AppRoute.PersonaList }
            )


        }

        is AppRoute.PerfilFuncional -> {

            val perfilViewModel: com.charirodriguez.abilityplus.ui.perfil.PerfilFuncionalViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    key = "perfil-${r.personaId}",
                    factory = com.charirodriguez.abilityplus.ui.perfil.PerfilFuncionalViewModelFactory(
                        personaId = r.personaId,
                        dao = db.PersonaDiagnosticoDao()
                    )
                )

            val cieSeleccionado by perfilViewModel.cieSeleccionado.collectAsState()

            val cifViewModel: com.charirodriguez.abilityplus.ui.perfil.PerfilFuncionalCifViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    key = "cif-${r.personaId}",
                    factory = com.charirodriguez.abilityplus.ui.perfil.PerfilFuncionalCifViewModelFactory(
                        personaId = r.personaId,
                        dao = db.personaCifDao()
                    )
                )


            val cifsSeleccionadas by cifViewModel.cifsSeleccionadas.collectAsState()
            val cifError by cifViewModel.error.collectAsState()

            val avdViewModel: com.charirodriguez.abilityplus.ui.avd.AvdViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    key = "avd-${r.personaId}",
                    factory = com.charirodriguez.abilityplus.ui.avd.AvdViewModelFactory(
                        personaId = r.personaId,
                        actividadDao = db.actividadBvdDao(),
                        valoracionDao = db.valoracionActividadDao()
                    )
                )

            val actividades by avdViewModel.actividades.collectAsState()
            val valoraciones by avdViewModel.valoraciones.collectAsState()
            val resultadoAvd by avdViewModel.resultado.collectAsState()
            val context = LocalContext.current

            PerfilFuncionalScreen(
                personaId = r.personaId,
                diagnosticos = diagnosticos,
                cifs = cifs,
                cifError = cifError,
                actividades = actividades,
                valoraciones = valoraciones,
                cieSeleccionado = cieSeleccionado,
                onSeleccionarCie = { codigo -> perfilViewModel.seleccionarCie(codigo) },
                cifsSeleccionadas = cifsSeleccionadas,
                resultadoAvd = resultadoAvd,
                onToggleCif = { codigo -> cifViewModel.toggleCif(codigo) },
                onCambiarSemaforo = { actividadId, semaforo ->
                    avdViewModel.setSemaforo(
                        actividadId,
                        semaforo
                    )
                },
                onVolverDatosPersonales = { route = AppRoute.PersonaForm(personaId = r.personaId) },
                onFinalizar = { route = AppRoute.PersonaList },
                onGenerarInforme = {
                        val data = com.charirodriguez.abilityplus.ui.report.InformePerfilData(
                            numeroExpediente = "EXP-${r.personaId}",
                            personaId = r.personaId,
                            cieCodigo = cieSeleccionado,
                            cifsSeleccionadas = cifsSeleccionadas,
                            rojos = resultadoAvd.rojos,
                            amarillos = resultadoAvd.amarillos,
                            verdes = resultadoAvd.verdes,
                            etiquetaResultado = resultadoAvd.etiqueta
                        )

                        com.charirodriguez.abilityplus.ui.report.generarPdfYCompartir(
                            context = context,
                            data = data
                        )
                    },

                    // luego lo relleno

                )
        }
    }
    }





