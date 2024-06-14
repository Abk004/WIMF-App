package com.example.wimf1

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.firebase.ui.auth.AuthUI
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Calendar
import java.util.concurrent.TimeUnit

@Suppress("MissingPermission")
class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID: String = "WIMFNotificationChannel"
    }

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        //Require the permission POST_NOTIFICATIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

        createNotificationChannel()


        val myWorkRequest: WorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = 1, // Ripeti ogni 1
            repeatIntervalTimeUnit = TimeUnit.DAYS // giorno
        ).build()

        // Enqueue the WorkRequest
        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(myWorkRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                signOut()
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // Reindirizza all'Activity di login
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel.
        val name = "Notification Channel"
        val descriptionText = "Channel for notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system. You can't change the importance
        // or other notification behaviors after this.
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }
}