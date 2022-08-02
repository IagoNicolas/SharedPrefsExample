package com.sidi.sharedprefsexample

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys

class MainActivity : Activity() {
    private val TAG: String = this::class.java.simpleName
    private var sharedpreferences: SharedPreferences? = null
    private var emailTextEdit: EditText? = null
    private var passTextEdit: EditText? = null

    companion object {
        // This is my Shared Preferences filename.
        const val mypreference = "mypreferences"
        // These are my Shared Preferences Keys.
        const val Email = "emailKey"
        const val Pass = "passKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailTextEdit = findViewById<View>(R.id.etEmail) as EditText
        passTextEdit = findViewById<View>(R.id.etPass) as EditText
        // This is where i declare my Shared Preferences variable.
        //sharedpreferences = getSharedPreferences(mypreference, MODE_PRIVATE)
        //sharedpreferences = getPreferences(MODE_PRIVATE)
        sharedpreferences = EncryptedSharedPreferences.create(
            applicationContext,
            mypreference,
            MasterKey.Builder(application)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveKeys(view: View?) {
        val email = emailTextEdit?.text.toString()
        val pass = passTextEdit?.text.toString()
        val editor = sharedpreferences?.edit()
        editor?.putString(Email, email)
        editor?.putString(Pass, pass)
        editor?.apply()

        Toast.makeText(this, "Credentials Saved", Toast.LENGTH_SHORT).show()
    }

    fun retrieveKeys(view: View?) {
        sharedpreferences = getSharedPreferences(mypreference, MODE_PRIVATE)
        if (sharedpreferences?.contains(Email)!!) {
            emailTextEdit?.setText(sharedpreferences?.getString(Email, ""))
        }
        if (sharedpreferences?.contains(Pass)!!) {
            passTextEdit?.setText(sharedpreferences?.getString(Pass, ""))
        }

        Toast.makeText(this, "Credentials Retrieved", Toast.LENGTH_SHORT).show()
    }

    fun clearFields(view: View?) {
        emailTextEdit?.setText("")
        passTextEdit?.setText("")

        Toast.makeText(this, "Fields Cleared", Toast.LENGTH_SHORT).show()
    }

    fun clearKeys(view: View?) {
        val editor = sharedpreferences?.edit()
        editor?.remove(Email)
        editor?.remove(Pass)
        if (editor != null) {
            if(editor.commit()) {
                Log.v(TAG, "Successfully modified")
            }
        }

        Toast.makeText(this, "Keys Cleared", Toast.LENGTH_SHORT).show()
    }
}
