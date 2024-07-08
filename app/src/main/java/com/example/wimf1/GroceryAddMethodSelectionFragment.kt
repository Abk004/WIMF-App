package com.example.wimf1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.wimf1.databinding.FragmentGroceryAddBinding
import com.example.wimf1.databinding.FragmentGroceryAddMethodSelectionBinding


class GroceryAddMethodSelectionFragment : Fragment() {

    private lateinit var binding: FragmentGroceryAddMethodSelectionBinding

    private val viewModel: GroceryListViewModel by activityViewModels()

    private var requestCamera: ActivityResultLauncher<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroceryAddMethodSelectionBinding.inflate(inflater, container, false)

        binding.buttonManualEntry.setOnClickListener() {
            addManualEntry(view = binding.root)
        }


        requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission(), ) {
            if (it) {
                // Permission has been granted
                addWithBarcode(view = binding.root)
            } else {
                // Permission has been denied
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()

            }
        }

        binding.buttonBarcodeScan.setOnClickListener {
            requestCamera?.launch(android.Manifest.permission.CAMERA)
        }

        return binding.root
    }

    private fun addWithBarcode(view: View) {
        val navController = Navigation.findNavController(view)
        navController.navigate(R.id.action_groceryAddMethodSelectionFragment_to_groceryAddBarCode)
    }

    private fun addManualEntry(view: View) {
        val navController = Navigation.findNavController(view)
        navController.navigate(R.id.action_groceryAddMethodSelectionFragment_to_groceryAddFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.title = "Choose method to add product"

        (activity as AppCompatActivity?)!!.findViewById<Toolbar>(R.id.my_toolbar)
            ?.setNavigationOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
    }


}