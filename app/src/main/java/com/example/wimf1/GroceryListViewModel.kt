package com.example.wimf1

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase




class GroceryListViewModel : ViewModel() {

    data class BarcodeData(
        val barcode: String,
        val name: String
    )

    private val db = Firebase.firestore

    private val _groceries = MutableLiveData<List<GroceryStructure>>()

    var fridgeName: String = ""
    private var groceryName: String = ""

    val groceries: LiveData<List<GroceryStructure>>
        get() = _groceries

    fun getGrocery(_fridgeName: String) {
        fridgeName = _fridgeName
        db.collection(FirebaseAuth.getInstance().currentUser!!.uid)
            .document(fridgeName)
            .collection("Grocery").get()
            .addOnSuccessListener { result ->
                val grocery = mutableListOf<GroceryStructure>()
                for (document in result) {
                    grocery.add(
                        GroceryStructure(
                            document.data["name"].toString(),
                            document.data["expirationDate"].toString(),
                            document.data["quantity"].toString(),
                            document.data["description"].toString(),

                            )
                    )
                }
                _groceries.postValue(grocery.sortedBy { it.expirationDate })
            }
    }

    fun addGrocery(grocery: GroceryStructure) {
        db.collection(FirebaseAuth.getInstance().currentUser!!.uid)
            .document(fridgeName)
            .collection("Grocery")
            .document(grocery.name)
            .set(grocery)
    }

    fun addGroceryBarCode(grocery: GroceryStructure, barcode: String) {
        db.collection("barcodes")
            .document(barcode)
            .set(BarcodeData(barcode, grocery.name))
    }

    fun deleteGroceryItem(groceryName: String) {
        db.collection(FirebaseAuth.getInstance().currentUser!!.uid)
            .document(fridgeName)
            .collection("Grocery")
            .document(groceryName)
            .delete().addOnSuccessListener {
                getGrocery(fridgeName)
            }

    }

    fun decrementGroceryQuantity(grocery: GroceryStructure) {
        val newQuantity = grocery.quantity.toInt() - 1
        db.collection(FirebaseAuth.getInstance().currentUser!!.uid)
            .document(fridgeName)
            .collection("Grocery")
            .document(grocery.name)
            .update("quantity", newQuantity.toString())
            .addOnSuccessListener {
                getGrocery(fridgeName)
            }

    }
}

