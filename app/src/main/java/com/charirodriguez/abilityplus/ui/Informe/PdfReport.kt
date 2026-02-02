package com.charirodriguez.abilityplus.ui.informe

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.content.ContextCompat.startActivity
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
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
        etiqueta: String
    ) {
        val pdfFile = generarPdf(
            context = context,
            expediente = expediente,
            cie = cie,
            cifs = cifs,
            rojos = rojos,
            amarillos = amarillos,
            verdes = verdes,
            etiqueta = etiqueta
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
        etiqueta: String
    ): File {
        val document = PdfDocument()
        val paint = Paint()

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 aprox
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        var y = 60

        fun line(text: String) {
            canvas.drawText(text, 40f, y.toFloat(), paint)
            y += 22
        }

        paint.textSize = 18f
        line("ABILITY+ — Informe (MVP)")
        y += 10

        paint.textSize = 14f
        line("Expediente: $expediente")
        line("Diagnóstico (CIE): ${cie ?: "No indicado"}")
        line("CIF seleccionadas: ${if (cifs.isEmpty()) "Ninguna" else cifs.joinToString(", ")}")
        y += 10
        line("AVD -> Rojos: $rojos  Amarillos: $amarillos  Verdes: $verdes")
        line("Resultado: $etiqueta")

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
