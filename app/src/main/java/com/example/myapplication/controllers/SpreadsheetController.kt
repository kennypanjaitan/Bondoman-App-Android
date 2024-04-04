package com.example.myapplication.controllers

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.example.myapplication.models.CategoryEnum
import com.example.myapplication.room.TransactionEntity
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
    private lateinit var dataStyle: XSSFCellStyle
    private lateinit var sheet: XSSFSheet

    fun writeSpreadsheet(context: Context, uri: Uri, data: List<TransactionEntity>) {
        workBook = XSSFWorkbook()
        sheet = workBook.createSheet("Transactions")
        setStyle()
        createHeaderRow()
        fillDataIntoSheet(data)
        writeDataToStream(context, uri)
    }

    private fun setStyle() {
        headerStyle = workBook.createCellStyle()
        dataStyle = workBook.createCellStyle()

        headerStyle.apply {
            fillForegroundColor = IndexedColors.AQUA.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
        }
        dataStyle.apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            shrinkToFit = true
        }
    }

    private fun setDataStyle(categoryEnum: CategoryEnum): XSSFCellStyle {
        val style = workBook.createCellStyle()
        style.apply {
            alignment = HorizontalAlignment.CENTER
            verticalAlignment = VerticalAlignment.CENTER
            shrinkToFit = true
            fillForegroundColor = when (categoryEnum) {
                CategoryEnum.INCOME -> IndexedColors.LIGHT_GREEN.index
                CategoryEnum.EXPENSE -> IndexedColors.RED.index
            }
            fillPattern = FillPatternType.SOLID_FOREGROUND
        }
        return style
    }

    private fun createHeaderRow() {
        val headerRow = sheet.createRow(0)
        val headers = arrayOf("ID", "Tanggal", "Kategori", "Nama", "Nominal", "Lokasi")
        headers.forEachIndexed { index, header ->
            headerRow.createCell(index).apply {
                setCellValue(header)
                cellStyle = headerStyle
            }
        }
    }

    private fun fillDataIntoSheet(transactionList: List<TransactionEntity>) {
        if (transactionList.isEmpty()) return
        for ((index, transaction) in transactionList.withIndex()) {
            val dataRow = sheet.createRow(index + 1)
            val coloredStyle = setDataStyle(transaction.category)
            dataRow.createCell(0).apply {
                setCellValue(transaction.id.toString())
                cellStyle = dataStyle
            }
            dataRow.createCell(1).apply {
                setCellValue(transaction.date)
                cellStyle = dataStyle
            }
            dataRow.createCell(2).apply {
                setCellValue(transaction.category.toString())
                cellStyle = coloredStyle
            }
            dataRow.createCell(3).apply {
                setCellValue(transaction.title)
                cellStyle = dataStyle
            }
            dataRow.createCell(4).apply {
                setCellValue(transaction.nominal.toDouble())
                cellStyle = coloredStyle
            }
            dataRow.createCell(5).apply {
                setCellValue(transaction.location)
                cellStyle = dataStyle
            }
        }
    }

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