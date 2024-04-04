package com.example.myapplication.ui.statistics

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null

    private lateinit var viewModel: StatisticsViewModel
    private lateinit var pieChart: PieChart
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        val root: View = binding.root
        pieChart = binding.pieChart
        createPieChart()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[StatisticsViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun createPieChart() {
        val list: ArrayList<PieEntry> = ArrayList()

        // TODO: Get data from database
        list.add(PieEntry(16000000f, "Income"))
        list.add(PieEntry(4000000f,"Expense"))

        val pieDataSet = PieDataSet(list, "Transactions")

        pieDataSet.apply {
            setColors(intArrayOf(
                Color.parseColor("#059669"),  // Income
                Color.parseColor("#BE123C")   // Expense
                ), 255
            )
            valueTextSize = 16f
            valueTextColor = ContextCompat.getColor(requireContext(), R.color.slate_50)
            valueFormatter = PercentFormatter(pieChart)
        }

        val pieData= PieData(pieDataSet)

        pieChart.apply {
            data = pieData
            description.text = "Pie Chart"
            setCenterTextSize(14f)
            setEntryLabelTextSize(14f)
            setUsePercentValues(true)
            animateY(1000)
            setHoleColor(Color.parseColor("#020617"))
            setCenterTextColor(Color.parseColor("#F8FAFC"))
            legend.apply {
                textColor = Color.parseColor("#F8FAFC")
                formSize = 12f
                textSize = 16f
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                orientation = Legend.LegendOrientation.VERTICAL
            }

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    if (e != null) {
                        centerText = "Rp${e.y.toULong()}"
                    }
                }

                override fun onNothingSelected() {
                    var total = 0f
                    for (entry in list) {
                        total += entry.value
                    }
                    centerText = "Total Transactions:\n Rp${total.toULong()}"
                }
            })
        }
    }
}