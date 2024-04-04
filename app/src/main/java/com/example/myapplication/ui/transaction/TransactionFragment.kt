package com.example.myapplication.ui.transaction

import TransactionViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.TransactionAdapter
import com.example.myapplication.databinding.FragmentTransactionBinding
import com.example.myapplication.room.TransactionEntity
import androidx.lifecycle.Observer

class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private val transactionViewModel: TransactionViewModel by activityViewModels {
        TransactionViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val allBtn: Button = binding.allFilter
        val expensesBtn: Button = binding.expensesFilter
        val incomeBtn: Button = binding.incomeFilter
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
        binding.addbutton.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.navigation_formTransaction)
        }

        recyclerView = binding.transactionView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        transactionAdapter = TransactionAdapter(arrayListOf(), object : TransactionAdapter.OnAdapterListener{
            override fun onClick(transaction: TransactionEntity) {
                intentDetail(transaction.id)
            }
        })
        recyclerView.adapter = transactionAdapter
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //observe live data
        transactionViewModel.listTransaction.observe(viewLifecycleOwner) { transactions ->
            transactionAdapter.setData(transactions)
        }

        //fetch data
        transactionViewModel.getAllData()
    }

    private fun intentDetail(transactionID: Int){
        val bundle = Bundle()
        bundle.putInt("transactionID", transactionID)
        findNavController().navigate(R.id.DetailTransaction, bundle)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}