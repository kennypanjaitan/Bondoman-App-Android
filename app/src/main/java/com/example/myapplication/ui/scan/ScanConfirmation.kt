package com.example.myapplication.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ScanConfirmationAdapter
import com.example.myapplication.databinding.FragmentScanBinding
import com.example.myapplication.databinding.FragmentScanConfirmationBinding

class ScanConfirmation: Fragment() {
    private var _binding: FragmentScanConfirmationBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var scanConfirmationAdapter: ScanConfirmationAdapter
    private val scanViewModel: ScanViewModel by activityViewModels {
        ScanViewModelFactory(requireContext())
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanConfirmationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setHasOptionsMenu(true)
        recyclerView = binding.transactionView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        scanConfirmationAdapter = ScanConfirmationAdapter(arrayListOf())
        recyclerView.adapter = scanConfirmationAdapter
        binding.savebtn.setOnClickListener {
            scanViewModel.submitTransactions()
            findNavController().navigate(R.id.navigation_transactions)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scanViewModel.scannedTransactionList.observe(viewLifecycleOwner, Observer { transactions ->
            scanConfirmationAdapter.setData(transactions)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle back button press
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}