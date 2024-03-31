package com.example.myapplication.ui.transaction

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
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
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

    private lateinit var category: ArrayList<String>
    private lateinit var adapterItems: ArrayAdapter<String>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var categoryS: String = ""
    private val binding get() = _binding!!
    private val transactionDB by lazy { TransactionDB(requireContext()) }
    private var address: String = ""

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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        autoCompleted.setAdapter(adapterItems)
        autoCompleted.inputType = EditorInfo.TYPE_NULL
        autoCompleted.onItemClickListener = AdapterView.OnItemClickListener {
            adapterView, view, i, l ->

            categoryS = adapterView.getItemAtPosition(i).toString()
        }

        binding.addtrans.setOnClickListener{
            addTransaction()
        }
        return root
    }

    @SuppressLint("SimpleDateFormat")
    private fun submit() {
        val title = binding.inputTitle.text.toString()
        val nominal = binding.inputNominal.text.toString().toInt()
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
        val currentDate = sdf.format(Date())
        if (inputValidated(title, nominal, categoryS)){
            var type = CategoryEnum.EXPENSE
            if (categoryS == "Income") {
                type = CategoryEnum.INCOME
            }
            CoroutineScope(Dispatchers.IO).launch {

                transactionDB.transactionDao().addTransaction(
                    TransactionEntity(0, title, currentDate, address, latitude, longitude, nominal, type)
                )
            }
            val navController = findNavController()
            navController.navigate(R.id.navigation_transactions)
        }
    }

    private fun addTransaction() {
        if (ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION )
            != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),100
            )
            return
    }

        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if (it!=null){
                val temp = it.latitude
                latitude = temp
                longitude = it.longitude
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                try {
                    val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
                    val obj: Address = addresses!![0]
                    address = obj.locality
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
                submit()
            }
        }
}


    private fun inputValidated(title: String, nominal: Int, category: String): Boolean{
        return title.isNotEmpty() && nominal != 0 && category.isNotEmpty()
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