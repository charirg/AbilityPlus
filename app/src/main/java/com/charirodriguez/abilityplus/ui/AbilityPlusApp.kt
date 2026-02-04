package com.charirodriguez.abilityplus.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.charirodriguez.abilityplus.R
import com.charirodriguez.abilityplus.data.local.db.AbilityPlusDatabase
import com.charirodriguez.abilityplus.data.repository.PersonaRepository
import com.charirodriguez.abilityplus.ui.navigation.AppRoute
import com.charirodriguez.abilityplus.ui.perfil.PerfilFuncionalScreen
import com.charirodriguez.abilityplus.ui.perfil.PerfilPersonaHeaderViewModel
import com.charirodriguez.abilityplus.ui.perfil.PerfilPersonaHeaderViewModelFactory
import com.charirodriguez.abilityplus.ui.persona.PersonaFormScreen
import com.charirodriguez.abilityplus.ui.persona.PersonaViewModel
import com.charirodriguez.abilityplus.ui.persona.PersonaViewModelFactory
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbilityPlusApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val db = com.charirodriguez.abilityplus.data.local.db.AbilityPlusDatabase.getInstance(context)
    val diagnosticoDao = db.diagnosticoCie10Dao()

    val diagnosticoViewModel: com.charirodriguez.abilityplus.ui.diagnostico.DiagnosticoViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel(
            factory = com.charirodriguez.abilityplus.ui.diagnostico.DiagnosticoViewModelFactory(
                diagnosticoDao
            )
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.logo_abilityplus),
                        contentDescription = "Logo AbilityPlus",
                        modifier = Modifier
                            .size(420.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(12.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(onClick = { route = AppRoute.PersonaList }) {
                        Text("Iniciar sesión")
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

            ) { padding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ExtendedFloatingActionButton(onClick = { route = AppRoute.PersonaForm(personaId = null)
                    }) {
                        Text("Crear nuevo expediente",
                        modifier = Modifier.fillMaxWidth()
                        )

                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Mostrar expedientes eliminados")
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
                                        if (persona.ultimoInformeMillis != null) {
                                            Text("Informe generado ✅", style = MaterialTheme.typography.bodySmall)
                                        }

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

                                                OutlinedButton(
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
                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = { route = AppRoute.Login },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Finalizar sesión")
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
                        dao = db.PersonaDiagnosticoDao(),
                        personaDao = db.personaDao()
                    )
                )
            LaunchedEffect(r.personaId) {
                perfilViewModel.asegurarFechaValoracion()
            }

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

            val headerViewModel: PerfilPersonaHeaderViewModel =
                viewModel(
                    factory = PerfilPersonaHeaderViewModelFactory(
                        personaId = r.personaId,
                        personaDao = db.personaDao()
                    )
                )

            val persona by headerViewModel.persona.collectAsState()

            LaunchedEffect(persona?.fechaValoracionMillis) {
                val p = persona ?: return@LaunchedEffect
                if (p.fechaValoracionMillis == null) {
                    val ahora = System.currentTimeMillis()
                    db.personaDao().setFechaValoracion(p.id, ahora)
                }
            }

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
                    avdViewModel.setSemaforo(actividadId, semaforo)
                },
                onVolverDatosPersonales = { route = AppRoute.PersonaForm(personaId = r.personaId) },
                onFinalizar = { route = AppRoute.PersonaList },
                numeroExpediente = persona?.numeroExpediente ?: "EXP-${r.personaId}",
                fechaValoracionMillis = persona?.fechaValoracionMillis,
                onGenerarInforme = {
                            val ahora = System.currentTimeMillis()
                            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO)
                                .launch {
                                    db.personaDao().setUltimoInforme(r.personaId, ahora)
                                }
                    com.charirodriguez.abilityplus.ui.informe.PdfReport.generarYCompartir(
                        context = context,
                        expediente = persona?.numeroExpediente ?: "EXP-${r.personaId}",
                        cie = cieSeleccionado,
                        cifs = cifsSeleccionadas,
                        rojos = resultadoAvd.rojos,
                        amarillos = resultadoAvd.amarillos,
                        verdes = resultadoAvd.verdes,
                        etiqueta = resultadoAvd.etiqueta,
                        fechaValoracionMillis = persona?.fechaValoracionMillis
                    )

                }

            )
        }
    }
}









