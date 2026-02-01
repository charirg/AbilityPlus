package com.charirodriguez.abilityplus.ui.cif

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.charirodriguez.abilityplus.data.local.entity.seed.FuncionalidadCifEntity

@Composable
fun FuncionalidadCifScreen(
    cifs: List<FuncionalidadCifEntity>,
    seleccionadas: Set<String>,
    onSeleccionar: (FuncionalidadCifEntity) -> Unit
) {
    LazyColumn {
        items(cifs) { cif ->
            val selected = seleccionadas.contains(cif.codigo)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onSeleccionar(cif) },
                colors = if (selected) {
                    CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                } else {
                    CardDefaults.cardColors()
                }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(cif.codigo, style = MaterialTheme.typography.titleMedium)
                    Text(cif.denominacion, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
