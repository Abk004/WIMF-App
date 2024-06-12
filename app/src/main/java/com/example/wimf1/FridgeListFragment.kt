package com.example.wimf1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.Navigation
import com.example.wimf1.databinding.FragmentFridgeListBinding
import com.example.wimf1.databinding.FragmentGroceryListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FridgeListFragment : Fragment() {

    private var _binding: FragmentFridgeListBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_fridge_list, container, false)
        _binding = FragmentFridgeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
            AddFridgeItem(view)
        }

        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(false)
    }

    fun AddFridgeItem(view: View) {
        val navController = Navigation.findNavController(view)
        navController.navigate(R.id.action_fridgeListFragment_to_fridgeAddFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}