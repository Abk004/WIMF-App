package com.example.wimf1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class WelcomeActivity : AppCompatActivity() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }

    val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    lateinit var signInIntent: Intent

    private lateinit var languages: MutableList<String>
    private var previousSelectedIndex: Int = 1
    private lateinit var languageSpinner: Spinner
    private lateinit var languageAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        languages = resources.getStringArray(R.array.languages).toMutableList()
        setContentView(R.layout.activity_welcome)
        applySavedLocale()
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupDropdown()

        if (FirebaseAuth.getInstance().currentUser?.uid != null) {
            val user = FirebaseAuth.getInstance().currentUser
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USER", user)
            startActivity(intent)
        }

        signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
    }




    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USER", user)
            startActivity(intent)
        } else {
            Log.e("AUTH", "Problemi con l'autenticazione")
        }
    }

    fun logIn(view: View) {
        signInLauncher.launch(signInIntent)
    }

    private fun applySavedLocale() {
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("language", "en") ?: "en"
        val locale = Locale(savedLanguage)
        languageSpinner = findViewById(R.id.languageSpinner)

        if (!::languageAdapter.isInitialized) {
            languageAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
            languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            languageSpinner.adapter = languageAdapter
        }

        val selectedIndex = languages.indexOf(getLanguageDisplayName(savedLanguage))
        if (selectedIndex != -1) {
            languageSpinner.setSelection(selectedIndex)
            previousSelectedIndex = selectedIndex
        }

        Locale.setDefault(locale)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        createConfigurationContext(configuration)
    }

    override fun attachBaseContext(newBase: Context) {
        val sharedPreferences = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("language", "en") ?: "en"
        val newLocale = Locale(savedLanguage)
        val configuration = newBase.resources.configuration
        configuration.setLocale(newLocale)
        val context = newBase.createConfigurationContext(configuration)
        super.attachBaseContext(context)
    }

    private fun updateLocale(locale: Locale) {
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("language", locale.language).apply()
        recreate()
    }

    private fun setupDropdown() {
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (position != previousSelectedIndex) {
                    previousSelectedIndex = position
                    val newLocale = when (position) {
                        0 -> Locale("en")
                        1 -> Locale("ar")
                        2 -> Locale("fr")
                        3 -> Locale("it")
                        else -> Locale("en")
                    }

                    updateLocale(newLocale)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getLanguageDisplayName(languageCode: String): String {
        val l = resources.getStringArray(R.array.languages)
        return when (languageCode) {
            "en" -> l[0]
            "ar" -> l[1]
            "fr" -> l[2]
            "it" -> l[3]
            else -> ""
        }
    }

}