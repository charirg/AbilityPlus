package com.charirodriguez.abilityplus.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfReport {

    fun generarInforme(
        context: Context,
        expediente: String,
        personaId: Long,
        cieSeleccionado: String?,
        cifsSeleccionadas: Set<String>,
        resultadoEtiqueta: String,
        resumenAvd: String
    ) {
        val pdf = PdfDocument()
        val paint = Paint()

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 aprox en puntos
        val page = pdf.startPage(pageInfo)
        val canvas = page.canvas

        var y = 60
        fun linea(text: String, salto: Int = 28) {
            canvas.drawText(text, 40f, y.toFloat(), paint)
            y += salto
        }

        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        paint.textSize = 18f
        linea("Ability+ — Informe de valoración", 40)

        paint.textSize = 12f
        linea("Fecha: $fecha")
        linea("Expediente: $expediente   (personaId=$personaId)")
        linea("")

        paint.textSize = 14f
        linea("Diagnóstico (CIE): ${cieSeleccionado ?: "No seleccionado"}", 34)

        paint.textSize = 12f
        linea("Funcionalidades (CIF) seleccionadas:")
        if (cifsSeleccionadas.isEmpty()) {
            linea(" - (ninguna)")
        } else {
            cifsSeleccionadas.sorted().forEach { linea(" - $it") }
        }

        linea("")
        paint.textSize = 14f
        linea("Resultado AVD: $resultadoEtiqueta", 34)

        paint.textSize = 12f
        linea("Resumen AVD:")
        linea(resumenAvd)

        pdf.finishPage(page)

        val file = File(context.cacheDir, "informe_${expediente}_${personaId}.pdf")
        FileOutputStream(file).use { out -> pdf.writeTo(out) }
        pdf.close()

        val uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(intent, "Abrir informe PDF"))
    }
}
