package com.example.wimf1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.wimf1.databinding.FragmentFridgeListBinding
import com.example.wimf1.databinding.FragmentGroceryAddBinding


class GroceryAddFragment : Fragment() {

    private var _binding: FragmentGroceryAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_grocery_add, container, false)
        _binding = FragmentGroceryAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        //actionBar?.title = "add products Item"

        (activity as AppCompatActivity?)!!.findViewById<Toolbar>(R.id.my_toolbar)
            ?.setNavigationOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}