package com.example.myapplication.ui.transaction

import TransactionViewModelFactory
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.adapter.LocationAdapter
import com.example.myapplication.databinding.FragmentFormTransactionBinding
import com.example.myapplication.models.CategoryEnum
import com.example.myapplication.room.TransactionDB
import com.example.myapplication.room.TransactionEntity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Date
import java.util.Locale


class FormTransaction: Fragment() {
    private var _binding: FragmentFormTransactionBinding? = null
    private val transactionViewModel: TransactionViewModel by viewModels { TransactionViewModelFactory(requireContext()) }

    private lateinit var category: ArrayList<String>
    private lateinit var adapterItems: ArrayAdapter<String>
    private lateinit var locationAdapter: LocationAdapter
    private var categoryS: String = ""
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTransactionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setHasOptionsMenu(true)
        category = ArrayList()
        category.add("Expense")
        category.add("Income")
        val autoCompleted = binding.autoCompleteTxt
        adapterItems = ArrayAdapter(requireContext(), R.layout.dropdown, category)
        locationAdapter = LocationAdapter(requireContext(),requireActivity())
        locationAdapter.getLocation()
        autoCompleted.setAdapter(adapterItems)
        autoCompleted.inputType = EditorInfo.TYPE_NULL
        autoCompleted.onItemClickListener = AdapterView.OnItemClickListener {
            adapterView, view, i, l ->

            categoryS = adapterView.getItemAtPosition(i).toString()
        }
        binding.addtrans.setOnClickListener{
            submit()
        }
        return root
    }

    @SuppressLint("SimpleDateFormat")
    private fun submit() {
        val title = binding.inputTitle.text.toString()
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
        val currentDate = sdf.format(Date())
        val nominal = binding.inputNominal.text.toString().toDouble()
        val address = locationAdapter.getAddress()
        val latitude = locationAdapter.getLatitude()
        val longitude = locationAdapter.getLongitude()
        if (inputValidated(title, nominal, categoryS)){
            var type = CategoryEnum.EXPENSE
            if (categoryS == "Income") {
                type = CategoryEnum.INCOME
            }
            transactionViewModel.insertTransaction(
                TransactionEntity(0, title, currentDate, address, latitude, longitude, nominal, type)
            )
            val navController = findNavController()
            navController.navigate(R.id.navigation_transactions)
        }
    }



    private fun inputValidated(title: String, nominal: Double, category: String): Boolean{
        return title.isNotEmpty() && nominal != 0.0 && category.isNotEmpty()
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