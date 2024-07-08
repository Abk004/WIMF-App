package com.example.wimf1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.wimf1.databinding.FragmentGroceryAddBarCodeBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException


class GroceryAddBarCode : Fragment() {

    val db = Firebase.firestore
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var binding: FragmentGroceryAddBarCodeBinding
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private var affidability = 0
    var intentData = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroceryAddBarCodeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize your image analyzer here or any other necessary setup
    }

    private fun iniBarCode() {
        barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true)
            //.setFacing(CameraSource.CAMERA_FACING_BACK)
            .build()


        binding.cameraPreview.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource.start(binding.cameraPreview.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(
                    requireContext(),
                    "Barcode scanner has been stopped",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {
                    binding.barcodeResult.post {
                        //binding.barcodeResult.text = barcodes.valueAt(0).displayValue
                        intentData = barcodes.valueAt(0).displayValue
                        binding.barcodeResult.text = intentData

                        // check in database
                        // bring to add fragment with database result as bundle data

                        db.collection("barcodes").document(intentData).get()
                            .addOnSuccessListener {
                                val bundle = Bundle()
                                bundle.putBoolean("isBarcode", true)
                                if (it.exists()) {
                                    bundle.putBoolean("inDatabase", true)
                                    bundle.putString("product_barcode", intentData)
                                    bundle.putString("product_name", it.getString("name"))
                                    affidability = 6
                                } else {
                                    bundle.putBoolean("inDatabase", false)
                                    bundle.putString("product_barcode", intentData)
                                }
                                affidability += 1

                                if (affidability > 5) {
                                        val navController = findNavController()
                                        val currentDestination = navController.currentDestination

                                        if (currentDestination?.id == R.id.groceryAddBarCode) {
                                            navController.navigate(R.id.action_groceryAddBarCode_to_groceryAddFragment, bundle)
                                        }
                                }

                            }
                    }
                }
            }
        }

        )


    }
    override fun onPause(){
        super.onPause()
        cameraSource.release()
    }
    override fun onResume() {
        super.onResume()
        iniBarCode()
    }

}
