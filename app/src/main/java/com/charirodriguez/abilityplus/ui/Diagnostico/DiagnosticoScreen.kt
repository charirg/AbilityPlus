package com.charirodriguez.abilityplus.ui.diagnostico

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.charirodriguez.abilityplus.data.local.entity.seed.DiagnosticoCie10Entity
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext

@Composable

fun DiagnosticosScreen(
    diagnosticos: List<DiagnosticoCie10Entity>,
    seleccionadoCodigo: String?,
    onSeleccionar: (DiagnosticoCie10Entity) -> Unit
)

 {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn {
            items(diagnosticos) { d ->
                val selected = d.codigo == seleccionadoCodigo
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onSeleccionar(d) },
                    colors = if (selected) {
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    } else {
                        CardDefaults.cardColors()
                    }
                )
                {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = d.codigo, style = MaterialTheme.typography.titleMedium)
                        Text(text = d.denominacion, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
