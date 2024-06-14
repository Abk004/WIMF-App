package com.example.wimf1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GroceryListViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _groceries = MutableLiveData<List<GroceryStructure>>()

    private var fridgeName: String = ""
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
                _groceries.postValue(grocery)
            }
    }

    fun addGrocery(grocery: GroceryStructure) {
        db.collection(FirebaseAuth.getInstance().currentUser!!.uid)
            .document(fridgeName)
            .collection("Grocery")
            .document(grocery.name)
            .set(grocery)
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