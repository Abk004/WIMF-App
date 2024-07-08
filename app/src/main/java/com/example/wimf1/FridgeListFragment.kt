package com.example.wimf1

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels

import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wimf1.databinding.FragmentFridgeListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FridgeListFragment : Fragment() {

    val db = Firebase.firestore
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var binding: FragmentFridgeListBinding

    val viewModel: FridgeListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFridgeListBinding.inflate(inflater, container, false)
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
        actionBar?.title = "W.I.M.F"

        binding.FridgeListRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.fridges.observe(viewLifecycleOwner) { _ ->
            binding.FridgeListRecyclerView.adapter =
                FridgeListRecycleView(requireContext(), viewModel)
        }

    }

    fun AddFridgeItem(view: View) {
        val navController = Navigation.findNavController(view)
        navController.navigate(R.id.action_fridgeListFragment_to_fridgeAddFragment)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFridges()
    }

}