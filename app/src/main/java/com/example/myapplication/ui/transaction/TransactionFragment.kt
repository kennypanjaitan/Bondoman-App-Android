package com.example.myapplication.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.TransactionAdapter
import com.example.myapplication.databinding.FragmentTransactionBinding
import com.example.myapplication.model.Transaction

class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionList: ArrayList<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transactionViewModel =
            ViewModelProvider(this).get(TransactionViewModel::class.java)

        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val allBtn: Button = binding.allFilter
        val expensesBtn: Button = binding.expensesFIlter
        val incomeBtn: Button = binding.incomeIlter
        allBtn.setOnClickListener {
            allBtn.setBackgroundResource(R.drawable.textlineactive)
            expensesBtn.setBackgroundResource(R.drawable.textlines)
            incomeBtn.setBackgroundResource(R.drawable.textlines)
        }
        expensesBtn.setOnClickListener {
            allBtn.setBackgroundResource(R.drawable.textlines)
            expensesBtn.setBackgroundResource(R.drawable.textlineactive)
            incomeBtn.setBackgroundResource(R.drawable.textlines)
        }
        incomeBtn.setOnClickListener {
            allBtn.setBackgroundResource(R.drawable.textlines)
            expensesBtn.setBackgroundResource(R.drawable.textlines)
            incomeBtn.setBackgroundResource(R.drawable.textlineactive)
        }

        recyclerView = binding.transactionView
//        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        transactionList = ArrayList()
        transactionList.add(Transaction("Proyek Tubes", "12 Desember 2024", "Koica", 100000))
        transactionList.add(Transaction("Pembelian Barang", "15 Desember 2024", "Supplier X", 50000))
        transactionList.add(Transaction("Gaji Karyawan", "20 Desember 2024", "Perusahaan ABC", 7500000))
        transactionList.add(Transaction("Pembayaran Sewa", "25 Desember 2024", "Pemilik Apartemen", 2000000))
        transactionList.add(Transaction("Pengisian Bahan Bakar", "28 Desember 2024", "SPBU A", 300000))




        transactionAdapter = TransactionAdapter(transactionList)
        recyclerView.adapter = transactionAdapter
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}