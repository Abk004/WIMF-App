package com.example.wimf1

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.wimf1.databinding.FragmentFridgeListBinding
import com.example.wimf1.databinding.FragmentGroceryAddBinding


class GroceryAddFragment : Fragment() {

    private lateinit var binding: FragmentGroceryAddBinding
    private var saveBarCode = false
    private var isBarcode = false
    private var barcode = ""

    private val viewModel: GroceryListViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroceryAddBinding.inflate(inflater, container, false)

        binding.buttonSubmit.setOnClickListener() {

            val grocery = GroceryStructure(
                binding.editTextProductName.text.toString(),
                binding.editTextExpirationDate.text.toString(),
                binding.editTextQuantity.text.toString(),
                binding.editTextDescription.text.toString()
            )

            viewModel.addGrocery(grocery)

            try {

                if (saveBarCode) {
                    viewModel.addGroceryBarCode(grocery, barcode)
                }
            } catch (e: Exception) {
                Log.d("GroceryAddFragment", "Error: $e")
            }


            findNavController().navigate(R.id.action_groceryAddFragment_to_groceryListFragment)

        }

        binding.buttonSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.editTextExpirationDate.setOnClickListener {
            showDatePickerDialog()
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonIncrement.setOnClickListener {
            incrementQuantity()
        }

        binding.buttonDecrement.setOnClickListener {
            decrementQuantity()
        }

        binding.editTextBarcode.visibility = View.GONE

        arguments?.let { bundle ->
            isBarcode = bundle.getBoolean("isBarcode")
            if (isBarcode){
                if (bundle.getBoolean("inDatabase")){
                    binding.editTextProductName.setText(bundle.getString("product_name"))
                } else {
                    saveBarCode = true
                }
                barcode = bundle.getString("product_barcode").toString()

                binding.editTextBarcode.visibility = View.VISIBLE
                binding.editTextBarcode.setText(barcode)
            }
        }

        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.title = getString(R.string.add_product)

        (activity as AppCompatActivity?)!!.findViewById<Toolbar>(R.id.my_toolbar)
            ?.setNavigationOnClickListener {

                activity?.onBackPressedDispatcher?.onBackPressed()
            }


    }

    private fun decrementQuantity() {
        var quantity = binding.editTextQuantity.text.toString().toInt()
        if (quantity > 0) {
            quantity--
        }
        binding.editTextQuantity.setText(quantity.toString())
    }

    private fun incrementQuantity() {
        var quantity = binding.editTextQuantity.text.toString().toInt()
        quantity++
        binding.editTextQuantity.setText(quantity.toString())
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Nota: selectedMonth è 0-indexed, quindi dobbiamo aggiungere 1
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.editTextExpirationDate.setText(formattedDate)
            }, year, month, day)

        datePickerDialog.show()
    }
}