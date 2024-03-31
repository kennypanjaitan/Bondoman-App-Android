package com.example.myapplication.ui.transaction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.TransactionAdapter
import com.example.myapplication.databinding.FragmentTransactionBinding
import com.example.myapplication.room.TransactionDB
import com.example.myapplication.room.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private val transactionDB by lazy { TransactionDB(requireContext()) }

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
//        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        transactionAdapter = TransactionAdapter(arrayListOf(), object : TransactionAdapter.OnAdapterListener{
            override fun onClick(transaction: TransactionEntity) {
                Log.d("memek", "DETAIL")
                intentDetail(transaction.id)
            }
        })
        CoroutineScope(Dispatchers.IO).launch {
//
            val transactionList = transactionDB.transactionDao().getAllTransactions()
            withContext(Dispatchers.Main) {
                transactionAdapter.setData(transactionList)
            }
        }
        recyclerView.adapter = transactionAdapter
        return root
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