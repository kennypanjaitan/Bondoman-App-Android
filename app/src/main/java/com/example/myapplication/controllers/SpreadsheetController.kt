package com.example.myapplication.controllers

import android.content.Context
import android.net.Uri
import android.widget.Toast
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

object SpreadsheetController {
    private lateinit var workBook: XSSFWorkbook
    private lateinit var headerStyle: XSSFCellStyle
    private lateinit var sheet: XSSFSheet

    fun writeSpreadsheet(context: Context, uri: Uri) {
        workBook = XSSFWorkbook()
        sheet = workBook.createSheet("Transactions")
        setHeaderStyle()
        createHeaderRow()
        writeDataToStream(context, uri)
    }

    private fun setHeaderStyle() {
        headerStyle = workBook.createCellStyle()
        headerStyle.apply {
            fillForegroundColor = IndexedColors.AQUA.getIndex()
            fillPattern = FillPatternType.SOLID_FOREGROUND
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
        }
    }

    private fun createHeaderRow() {
        val headerRow = sheet.createRow(0)
        val headers = arrayOf("Tanggal", "Kategori", "Nama", "Nominal", "Lokasi")
        headers.forEachIndexed { index, header ->
            headerRow.createCell(index).apply {
                setCellValue(header)
                cellStyle = headerStyle
            }
        }
    }

    /**
     * TODO:
     * 1. Refactor Transaction package name (models)
     * 2. Make tostring method in Transaction
     */
//    private fun fillDataIntoSheet(transactionList: List<Transaction>) {
//        for ((index, transaction) in transactionList.withIndex()) {
//            val row = sheet.createRow(index + 1)
//            row.createCell(0).setCellValue(transaction.date)
////            row.createCell(1).setCellValue(transaction.category)
//            row.createCell(2).setCellValue(transaction.title)
//            row.createCell(3).setCellValue(transaction.nominal.toDouble())
//            row.createCell(4).setCellValue(transaction.loc)
//        }
//    }

    private fun writeDataToStream(context: Context, uri: Uri): Boolean {
        var isWritten = false

        try {
            context.contentResolver.openFileDescriptor(uri, "w")?.use {
                val outputStream = FileOutputStream(it.fileDescriptor)
                workBook.write(outputStream)
                outputStream.flush()
                outputStream.close()
            }
            Toast.makeText(context, "File Created!", Toast.LENGTH_SHORT).show()
            isWritten = true
        } catch (e: Exception) {
            Toast.makeText(context, "File Creation Failed!", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        } finally {
            workBook.close()
        }

        return isWritten
    }
}