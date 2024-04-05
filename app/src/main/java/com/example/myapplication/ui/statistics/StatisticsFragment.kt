package com.example.myapplication.ui.statistics

import StatisticsViewModelFactory
import TransactionViewModelFactory
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
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
import java.lang.Exception

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null

    private lateinit var viewModel: StatisticsViewModel
    private lateinit var pieChart: PieChart
    private var income: Float = 0.0F
    private var expense: Float = 0.0F
    private val binding get() = _binding!!
    private val statisticsViewModel: StatisticsViewModel by activityViewModels {
        StatisticsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        val root: View = binding.root
        pieChart = binding.pieChart

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statisticsViewModel.valueIncome.observe(viewLifecycleOwner, Observer {value ->
            income = value.toFloat()
            createPieChart()
        })
        statisticsViewModel.valueExpense.observe(viewLifecycleOwner, Observer { value ->
            expense = value.toFloat()
            createPieChart()
        })

        statisticsViewModel.incomeTransaction()
        statisticsViewModel.expenseTransaction()
    }

    private fun createPieChart() {

        val list: ArrayList<PieEntry> = ArrayList()
        list.add(PieEntry( income, "Income"))
        list.add(PieEntry( expense,"Expense"))

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