package com.example.wimf1

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.wimf1.databinding.GroceryItemListBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class GroceryListRecycleView(
    var mContext: Context,
    private val viewModel: GroceryListViewModel,
) : RecyclerView.Adapter<GroceryListRecycleView.GroceryListHolder>() {

    inner class GroceryListHolder(val view: GroceryItemListBinding) :
        RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryListHolder {
        val binding =
            GroceryItemListBinding.inflate(LayoutInflater.from(mContext), parent, false)

        return GroceryListHolder(binding)
    }

    override fun getItemCount(): Int {
        return viewModel.groceries.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: GroceryListHolder, position: Int) {
        val grocery = viewModel.groceries.value?.get(position)!!
        val view = holder.view
        view.GroceryItemListTextViewName.text = grocery.name
        view.GroceryItemListTextViewDescription.text = grocery.description
        view.GroceryItemListTextViewQuantity.text = grocery.quantity
        view.GroceryItemListTextViewExpiryDate.text = grocery.expirationDate


        view.GroceryItemListDecrementButton.setOnClickListener {
            if (grocery.quantity.toInt() > 1) {
                viewModel.decrementGroceryQuantity(grocery)
            } else {
                viewModel.deleteGroceryItem(grocery.name)
            }
        }

        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


        val date = dateFormat.parse(grocery.expirationDate)
        cal.time = date!!

        if (cal.timeInMillis - System.currentTimeMillis() < 3 * 24 * 60 * 60 * 1000) {
            view.GroceryItemListTextViewExpiryDate.setTextColor(mContext.getColor(R.color.red))
        } else if (cal.timeInMillis - System.currentTimeMillis() < 7 * 24 * 60 * 60 * 1000) {
            view.GroceryItemListTextViewExpiryDate.setTextColor(mContext.getColor(R.color.yellow))
        } else {
            view.GroceryItemListTextViewExpiryDate.setTextColor(mContext.getColor(R.color.green))
        }

        view.cardView.setOnLongClickListener {
            AlertDialog.Builder(mContext)
                .setTitle("Delete Grocery Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("yes") { dialog, _ ->
                    viewModel.deleteGroceryItem(grocery.name)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()

            true
        }

    }
}
