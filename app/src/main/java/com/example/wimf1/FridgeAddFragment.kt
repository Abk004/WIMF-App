package com.example.wimf1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import com.example.wimf1.databinding.FragmentFridgeAddBinding
import com.example.wimf1.databinding.FragmentFridgeListBinding

class FridgeAddFragment : Fragment() {

    private lateinit var binding: FragmentFridgeAddBinding

    val viewModel: FridgeListViewModel by activityViewModels()

    var isEdit = false
    var fridgeName = ""
    var oldFridgeName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFridgeAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            isEdit = bundle.getBoolean("isEdit")
            if (isEdit) {
                oldFridgeName = bundle.getString("fridgeName").toString()
                binding.editTextFridgeName.setText(bundle.getString("fridgeName"))
                binding.editTextFridgeDescription.setText(bundle.getString("fridgeDescription"))
            }
        }

        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)


        (activity as AppCompatActivity?)!!.findViewById<Toolbar>(R.id.my_toolbar)
            ?.setNavigationOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

        if (isEdit) {
            binding.buttonSubmit.text = getString(R.string.update)
            binding.buttonDelete.visibility = View.VISIBLE
            actionBar?.title = getString(R.string.update_fridge)


            fridgeName = binding.editTextFridgeName.text.toString()
            binding.buttonDelete.setOnClickListener {
                viewModel.deleteFridge(fridgeName)
                activity?.onBackPressed()
            }
        } else {
            binding.buttonSubmit.text = getString(R.string.add)
            binding.buttonDelete.visibility = View.GONE
            actionBar?.title = getString(R.string.add_fridge)
        }


        binding.buttonSubmit.setOnClickListener {

            val fridge = FridgeStructure(
                binding.editTextFridgeName.text.toString(),
                binding.editTextFridgeDescription.text.toString()
            )

            if (isEdit) {
                viewModel.updateFridge(fridge, requireContext(), oldFridgeName)
            } else {
                viewModel.addFridge(fridge, requireContext())
            }

            activity?.onBackPressed()
        }

    }


}