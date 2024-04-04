package com.example.myapplication.ui.transaction

import TransactionViewModelFactory
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDetailTransactionBinding
import com.example.myapplication.models.CategoryEnum
import com.example.myapplication.room.TransactionDB
import com.example.myapplication.room.TransactionEntity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class DetailTransaction: Fragment(), OnMapReadyCallback{
    private var _binding: FragmentDetailTransactionBinding? = null
    private val transactionViewModel: TransactionViewModel by viewModels { TransactionViewModelFactory(requireContext()) }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var gMap: GoogleMap
    private val binding get() = _binding!!
    private var transactionID: Int = 0
    private lateinit var category: ArrayList<String>
    private lateinit var adapterItems: ArrayAdapter<String>
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var address: String = ""
    private var categoryS: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_NETWORK_STATE )
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_NETWORK_STATE),100
            )
        }
        _binding = FragmentDetailTransactionBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        transactionID = arguments?.getInt("transactionID")!!

        binding.savebtn.setOnClickListener{
            saveTransaction()
        }
        binding.viewLoc.setOnClickListener {
            viewLocation()
        }
        binding.updateLoc.setOnClickListener {
            updateLocation()
        }
        binding.deletebtn.setOnClickListener {
            showDialog("Delete Transaction")
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionViewModel.transaction.observe(requireActivity()){transaction ->
            setLabel(transaction)
        }

        //fetch data
        transactionViewModel.getTransaction(transactionID)
    }

    private fun setLabel(transaction: TransactionEntity) {
        binding.inputTitle.setText(transaction.title)
        binding.inputNominal.setText(transaction.nominal.toString())
        binding.inputDate.setText(transaction.date)
        latitude = transaction.latitude
        longitude = transaction.longitude
        val location = LatLng(latitude,longitude)
        gMap.addMarker(MarkerOptions().position(location).title(address))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16F))
        address = transaction.location
        category = ArrayList()
        category.add("Expense")
        category.add("Income")
        categoryS = transaction.category.toString()
        val autoCompleted = binding.autoCompleteTxt
        adapterItems = ArrayAdapter(requireContext(), R.layout.dropdown, category)
        autoCompleted.setAdapter(adapterItems)
        autoCompleted.inputType = EditorInfo.TYPE_NULL
        autoCompleted.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->
                categoryS = adapterView.getItemAtPosition(i).toString()
        }
        if (transaction.category.toString() == "EXPENSE") {
            autoCompleted.setText(R.string.filter_expense)
        } else if (transaction.category.toString() == "INCOME"){
            autoCompleted.setText(R.string.filter_income)
        }
    }

    private fun updateLocation(){
        if (ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION )
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),100
            )
        }
        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if (it!=null){
                Log.d("bewe", "masuk")
                val temp = it.latitude
                latitude = temp
                longitude = it.longitude

                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                try {
                    val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
                    val obj: Address = addresses!![0]
                    address = obj.locality
                    val location = LatLng(latitude,longitude)
                    gMap.resetMinMaxZoomPreference()
                    gMap.addMarker(MarkerOptions().position(location).title(address))
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16F))
                } catch (e: IOException) {
                    Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun viewLocation(){
        val geoUri = "http://maps.google.com/maps?q=loc:$latitude,$longitude Location Transaction"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
        requireContext().startActivity(intent)
    }

    private fun saveTransaction(){
        val title = binding.inputTitle.text.toString()
        val nominal = binding.inputNominal.text.toString().toInt()
        val date = binding.inputDate.text.toString()
        if (inputValidated(title, nominal, categoryS, date)){
            var type = CategoryEnum.EXPENSE
            if (categoryS == "Income") {
                type = CategoryEnum.INCOME
            }
            transactionViewModel.updateTransaction(TransactionEntity(transactionID, title, date, address, latitude, longitude, nominal, type))
            val navController = findNavController()
            navController.navigate(R.id.navigation_transactions)
        }
    }
    private fun showDialog(title: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setMessage("Are you sure you want to delete this transaction?")
            .setTitle("Delete Transaction")
            .setPositiveButton("Delete") { dialog, which ->
                transactionViewModel.transaction.value?.let {
                    transactionViewModel.deleteTransaction(
                        it
                    )
                }
                dialog.dismiss()
                findNavController().navigate(R.id.navigation_transactions)
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun inputValidated(title: String, nominal: Int, category: String, date: String): Boolean{
        return title.isNotEmpty() && nominal != 0 && category.isNotEmpty() && date.isNotEmpty()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}