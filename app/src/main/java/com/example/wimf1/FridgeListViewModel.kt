package com.example.wimf1

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FridgeListViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _fridges = MutableLiveData<List<FridgeStructure>>()
    val fridges: LiveData<List<FridgeStructure>>
        get() = _fridges

    fun getFridges() {
        db.collection(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { result ->
                val fridges = mutableListOf<FridgeStructure>()
                for (document in result) {
                    fridges.add(
                        FridgeStructure(
                            document.data["name"].toString(),
                            document.data["description"].toString()
                        )
                    )
                }
                _fridges.postValue(fridges)
            }
    }

    fun addFridge(fridge: FridgeStructure, context: Context) {
        val collection = db.collection(FirebaseAuth.getInstance().currentUser!!.uid)

        collection.document(fridge.name).get().addOnSuccessListener {
            if (it.exists()) {
                Toast.makeText(context, "Fridge already exist", Toast.LENGTH_SHORT).show()
            } else {
                collection.document(fridge.name)
                    .set(fridge).addOnSuccessListener {
                        getFridges()
                    }
            }
        }
    }

    fun updateFridge(fridge: FridgeStructure, requireContext: Context, oldFridgeName: String) {
        val collection = db.collection(FirebaseAuth.getInstance().currentUser!!.uid)

        collection.document(fridge.name).get().addOnSuccessListener {
            if (fridge.name == oldFridgeName) {
                collection.document(fridge.name)
                    .set(fridge).addOnSuccessListener {
                        getFridges()
                    }
            } else if (it.exists()) {
                Toast.makeText(requireContext, "Fridge already exist", Toast.LENGTH_SHORT).show()
            } else {
                collection.document(oldFridgeName).get().addOnSuccessListener {
                    collection.document(fridge.name).set(fridge).addOnSuccessListener {
                        collection.document(oldFridgeName).collection("Grocery").get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    val grocery = GroceryStructure(
                                        document.data["name"].toString(),
                                        document.data["expirationDate"].toString(),
                                        document.data["quantity"].toString(),
                                        document.data["description"].toString(),
                                    )
                                    collection.document(fridge.name).collection("Grocery")
                                        .document(grocery.name).set(grocery)
                                }
                                collection.document(oldFridgeName).delete()
                                getFridges()
                            }
                    }
                }
            }
        }
    }

    fun deleteFridge(fridgeName: String) {
        db.collection(FirebaseAuth.getInstance().currentUser!!.uid).document(fridgeName).delete()
    }
}

