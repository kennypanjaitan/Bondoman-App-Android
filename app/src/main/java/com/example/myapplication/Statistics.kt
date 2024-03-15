package com.example.myapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class Statistics : AppCompatActivity() {

    lateinit var pieChart: PieChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        pieChart=findViewById(R.id.pie_chart)



        val list: ArrayList<PieEntry> = ArrayList()

        list.add(PieEntry(60f, "abc"))
        list.add(PieEntry(40f,"def"))

        val pieDataSet = PieDataSet(list, "List")

        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)

        pieDataSet.valueTextSize=40f

        pieDataSet.valueTextColor= Color.BLACK

        val pieData= PieData(pieDataSet)

        pieChart.data = pieData

        pieChart.description.text = "Pie Chart"

        pieChart.centerText = "List"

        pieChart.animateY(2000)
    }
}