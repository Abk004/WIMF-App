package com.example.wimf1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.wimf1.databinding.FragmentFridgeListBinding
import com.example.wimf1.databinding.FridgeItemListBinding

class FridgeListRecycleView(
    var mContext: Context,
    private val viewModel: FridgeListViewModel,
) : RecyclerView.Adapter<FridgeListRecycleView.FridgeListHolder>() {

    inner class FridgeListHolder(val view: FridgeItemListBinding) :
        RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FridgeListHolder {
        val binding =
            FridgeItemListBinding.inflate(LayoutInflater.from(mContext), parent, false)

        return FridgeListHolder(binding)
    }

    override fun getItemCount(): Int {
        return viewModel.fridges.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: FridgeListHolder, position: Int) {
        val fridge = viewModel.fridges.value?.get(position)!!
        val view = holder.view
        view.FridgeItemListTextView.text = fridge.name
        view.cardView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("fridgeName", fridge.name)
            Navigation.findNavController(it)
                .navigate(R.id.action_fridgeListFragment_to_groceryListFragment, bundle)
        }
        view.cardView.setOnLongClickListener {
            val bundle = Bundle()
            bundle.putBoolean("isEdit", true)
            bundle.putString("fridgeName", fridge.name)
            bundle.putString("fridgeDescription", fridge.description)
            Navigation.findNavController(it)
                .navigate(R.id.action_fridgeListFragment_to_fridgeAddFragment, bundle)
            true
        }

    }

}
