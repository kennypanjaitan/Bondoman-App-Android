package com.example.myapplication.ui.dashboard

import android.Manifest
import android.app.Activity.MODE_PRIVATE
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.view.LifecycleCameraController
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentDashboardBinding
import com.example.myapplication.utils.appSettingOpen
import com.example.myapplication.utils.warningPermissionDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.content.Context
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var capture: ImageButton;
    private lateinit var flipCamera: ImageButton;
    private val multiplePermissionId = 14
    private val multiplePermissionNameList =
        arrayListOf(
            android.Manifest.permission.CAMERA,
        )
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var cameraSelector: CameraSelector
    private var orientationEventListener: OrientationEventListener? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var bitmap: Bitmap


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                requireActivity(), CAMERAX_PERMISSIONS, 0
            )
        }
        displayCamera()
        if (hasRequiredPermissions()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), CAMERAX_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        binding.gallerybtn.setOnClickListener {
            openGallery()
        }
        binding.capturebtn.setOnClickListener{
            takePhoto()
        }
        binding.retakebtn.setOnClickListener{
            displayCamera()
        }
        binding.uploadbtn.setOnClickListener{
            uploadPict()
        }
        cameraExecutor = Executors.newSingleThreadExecutor()
        return root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK){
            binding.imagePreview.setImageURI(data?.data)
            displayPhoto()
        }
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {

            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    val rotationDegrees = image.imageInfo.rotationDegrees
                    bitmap = image.toBitmap()
                    if (rotationDegrees != 0) {
                        val matrix = Matrix()
                        matrix.postRotate(rotationDegrees.toFloat())
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                    }
                    binding.imagePreview.setImageBitmap(bitmap)
                    displayPhoto()
                }
            }
        )
    }

    private fun uploadPict() {
        val requestFile = bitmap.toString().toRequestBody("image/jpg".toMediaTypeOrNull())
//        val body = MultipartBody.Part.createFormData("file", "image.jpg",requestFile)
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addPart(MultipartBody.Part.createFormData("file", "image.jpg", requestFile))
            .build()

        val sharedPreferences = requireContext().getSharedPreferences("token", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        val client = OkHttpClient()
        val url = "https://pbd-backend-2024.vercel.app/api/bill/upload"

        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Authorization", "Bearer ${token}")
//            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuaW0iOiIxMzUyMTAwMSIsImlhdCI6MTcxMTk4NTUwOSwiZXhwIjoxNzExOTg1ODA5fQ.g0gH2VM6E361jLjdkyfINN1d3yh2HG9z_js0LuShuf0")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Service Error", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    val jsonResponse = JSONObject(response.body!!.string())

                    val itemsObject = jsonResponse.getJSONObject("items")
                    val itemsArray = itemsObject.getJSONArray("items")

                    var totalPrice = 0.0

                    for(i in 0 until itemsArray.length()){
                        val item = itemsArray.getJSONObject(i)
                        val price = item.getDouble("price") * item.getInt("qty")
                        totalPrice += price
                    }

                    // yg di ambil nanti
                    // val title = itemsArray.getJSONObject(0).getString("name")
                    // totalPrice

                    Log.d("asd", jsonResponse.toString())
                    Log.d("asd", "total : $totalPrice")
                }
            }

        })

    }


    private fun displayPhoto() {
        binding.imagePreview.visibility = View.VISIBLE
        binding.cameraPreview.visibility = View.GONE
        binding.capturebtn.visibility = View.GONE
        binding.gallerybtn.visibility = View.GONE
        binding.retakebtn.visibility = View.VISIBLE
        binding.uploadbtn.visibility = View.VISIBLE
    }

    private fun displayCamera() {
        binding.imagePreview.visibility = View.GONE
        binding.cameraPreview.visibility = View.VISIBLE
        binding.capturebtn.visibility = View.VISIBLE
        binding.gallerybtn.visibility = View.VISIBLE
        binding.retakebtn.visibility = View.GONE
        binding.uploadbtn.visibility = View.GONE
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
        )
        private const val REQUEST_CODE_PERMISSIONS = 20
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }
}