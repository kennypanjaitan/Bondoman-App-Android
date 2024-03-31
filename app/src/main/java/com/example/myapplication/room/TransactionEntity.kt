package com.example.myapplication.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.models.CategoryEnum


@Entity(tableName = "transaction")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "Latitude")
    val latitude: Double,

    @ColumnInfo(name = "Longitude")
    val longitude: Double,

    @ColumnInfo(name = "nominal")
    val nominal: Int,

    @ColumnInfo(name = "category")
    val category: CategoryEnum
)
