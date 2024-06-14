package com.example.wimf1

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.firebase.ui.auth.AuthUI
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    private val db = Firebase.firestore
    override fun doWork(): Result {
        performBackgroundTask()
        return Result.success()
    }

    @Suppress("MissingPermission")
    private fun sendNotification() {
        val builder = NotificationCompat.Builder(applicationContext, MainActivity.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Uno o più prodotti stanno per scadere!")
            .setContentText("Vi consigliamo di controllare la data di scadenza dei prodotti presenti nei suoi frigi.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1, builder.build())
        }
    }

    private fun performBackgroundTask() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            return
        }

        db.collection(FirebaseAuth.getInstance().currentUser!!.uid).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    db.collection(FirebaseAuth.getInstance().currentUser!!.uid)
                        .document(document.data["name"].toString()).collection("Grocery").get()
                        .addOnSuccessListener { groceryResult ->

                            for (groceryDocument in groceryResult) {
                                val cal = Calendar.getInstance()
                                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                val date =
                                    dateFormat.parse(groceryDocument.data["expirationDate"].toString())
                                cal.time = date!!

                                if (cal.timeInMillis - System.currentTimeMillis() < 24 * 60 * 60 * 1000) {
                                    sendNotification()
                                }
                            }
                        }
                }
            }
    }


}







