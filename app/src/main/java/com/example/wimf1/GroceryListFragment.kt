package com.example.wimf1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wimf1.databinding.FragmentGroceryListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class GroceryListFragment : Fragment() {

    val db = Firebase.firestore
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var binding: FragmentGroceryListBinding

    val viewModel: GroceryListViewModel by activityViewModels()

    private var fridgeName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            fridgeName = bundle.getString("fridgeName").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGroceryListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            addGroceryItem(view)
        }

        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.title = "Grocery List of $fridgeName"


        binding.GroceryLisRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.groceries.observe(viewLifecycleOwner) { _ ->
            binding.GroceryLisRecyclerView.adapter =
                GroceryListRecycleView(requireContext(), viewModel)
        }

        viewModel.getGrocery(fridgeName)


        (activity as AppCompatActivity?)!!.findViewById<Toolbar>(R.id.my_toolbar)
            ?.setNavigationOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

    }

    private fun addGroceryItem(view: View) {
        val navController = Navigation.findNavController(view)
        navController.navigate(R.id.action_groceryListFragment_to_groceryAddMethodSelectionFragment)

    }


}