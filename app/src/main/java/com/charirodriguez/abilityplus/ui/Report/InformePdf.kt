package com.charirodriguez.abilityplus.ui.report

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

data class InformePerfilData(
    val numeroExpediente: String,
    val personaId: Long,
    val cieCodigo: String?,
    val cifsSeleccionadas: Set<String>,
    val rojos: Int,
    val amarillos: Int,
    val verdes: Int,
    val etiquetaResultado: String
)

fun generarPdfYCompartir(
    context: Context,
    data: InformePerfilData
) {
    // 1) Crear PDF en memoria
    val pdf = PdfDocument()

    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 aprox
    val page = pdf.startPage(pageInfo)
    val canvas = page.canvas

    val paintTitle = Paint().apply { textSize = 18f; isFakeBoldText = true }
    val paint = Paint().apply { textSize = 12f }

    var y = 60f
    fun line(text: String, p: Paint = paint) {
        canvas.drawText(text, 40f, y, p)
        y += 22f
    }

    line("ABILITY+ — Informe de perfil funcional", paintTitle)
    y += 10f

    line("Expediente: ${data.numeroExpediente}")
    line("PersonaId: ${data.personaId}")
    line("Diagnóstico CIE: ${data.cieCodigo ?: "Sin seleccionar"}")
    y += 10f

    line("Funcionalidades CIF seleccionadas:")
    if (data.cifsSeleccionadas.isEmpty()) {
        line(" - (ninguna)")
    } else {
        data.cifsSeleccionadas.sorted().forEach { codigo ->
            line(" - $codigo")
        }
    }
    y += 10f

    line("AVD (semaforización):")
    line(" - Rojos: ${data.rojos}")
    line(" - Amarillos: ${data.amarillos}")
    line(" - Verdes: ${data.verdes}")
    y += 10f
    line("Resultado: ${data.etiquetaResultado}", paintTitle)

    pdf.finishPage(page)

    // 2) Guardar en cache
    val file = File(context.cacheDir, "AbilityPlus_informe_${data.numeroExpediente}.pdf")
    FileOutputStream(file).use { out ->
        pdf.writeTo(out)
    }
    pdf.close()

    // 3) Compartir con FileProvider
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent, "Compartir informe PDF"))
}
