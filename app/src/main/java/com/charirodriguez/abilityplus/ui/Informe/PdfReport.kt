package com.charirodriguez.abilityplus.ui.informe

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object PdfReport {

    fun generarYCompartir(
        context: Context,
        expediente: String,
        cie: String?,
        cifs: Set<String>,
        rojos: Int,
        amarillos: Int,
        verdes: Int,
        etiqueta: String,
        fechaValoracionMillis: Long?

    ) {
        val pdfFile = generarPdf(
            context = context,
            expediente = expediente,
            cie = cie,
            cifs = cifs,
            rojos = rojos,
            amarillos = amarillos,
            verdes = verdes,
            etiqueta = etiqueta,
            fechaValoracionMillis = fechaValoracionMillis
        )


        val uri: Uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            pdfFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(context, Intent.createChooser(shareIntent, "Compartir informe PDF"), null)
    }


    private fun generarPdf(
        context: Context,
        expediente: String,
        cie: String?,
        cifs: Set<String>,
        rojos: Int,
        amarillos: Int,
        verdes: Int,
        etiqueta: String,
        fechaValoracionMillis: Long?
    ): File {


        val document = PdfDocument()

        val paintTitle = Paint().apply {
            textSize = 18f
            isFakeBoldText = true
        }

        val paint = Paint().apply {
            textSize = 14f
        }

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        var y = 60

        fun line(text: String, p: Paint = paint) {
            canvas.drawText(text, 40f, y.toFloat(), p)
            y += 22
        }
        val fechaTexto = fechaValoracionMillis?.let { millis ->
            java.time.Instant.ofEpochMilli(millis)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate()
                .toString()
        } ?: "—"

        // ---- CABECERA ----
        line("ABILITY+ Valoración del desempeño funcional", paintTitle)
        y += 10

        // ---- DATOS GENERALES ----
        line("Expediente: $expediente")
        line("Fecha de valoración: $fechaTexto")
        line("Diagnóstico (CIE): ${cie ?: "No indicado"}")
        y += 10

        // ---- CIF ----
        line("Funcionalidades CIF seleccionadas:")
        if (cifs.isEmpty()) {
            line(" - Ninguna")
        } else {
            cifs.forEach { codigo ->
                line(" - $codigo")
            }
        }

        y += 10

        // ---- AVD ----
        line("Valoración de las actividades de la vida diaria - AVD -:")
        line(" - Actividades con desempeño funcional pasivo: $rojos")
        line(" - Actividades con desempeño funcional asistido: $amarillos")
        line(" - Actividades con desempeño funcional autónomo: $verdes")

        y += 10

        // ---- RESULTADO ----
        line("Resultado de la valoración:")
        line(etiqueta, paintTitle)

        document.finishPage(page)

        val outDir = File(context.cacheDir, "informes")
        outDir.mkdirs()

        val file = File(outDir, "informe_${expediente}.pdf")
        FileOutputStream(file).use { fos ->
            document.writeTo(fos)
        }

        document.close()

        return file
    }
}

