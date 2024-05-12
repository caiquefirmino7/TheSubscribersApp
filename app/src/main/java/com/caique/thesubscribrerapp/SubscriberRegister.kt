package com.caique.thesubscribrerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.caique.thesubscribrerapp.data.entity.SubscriberEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class SubscriberRegister : AppCompatActivity() {

    private lateinit var nameLogin: EditText
    private lateinit var emailLogin: EditText
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscriber_register)

        initViews()
        btnSubmit()

        nameLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.isNotEmpty() && text[0].isLowerCase()) {
                    nameLogin.setText(text.substring(0, 1).uppercase(Locale.ROOT) + text.substring(1))
                    nameLogin.setSelection(nameLogin.length())
                }
            }
        })

    }

    private fun initViews() {
        nameLogin = findViewById(R.id.editTextName)
        emailLogin = findViewById(R.id.editTextEmail)
        buttonLogin = findViewById(R.id.buttonLogin)
    }

    private fun btnSubmit() {
        buttonLogin.setOnClickListener {
            val name = nameLogin.text.toString()
            val email = emailLogin.text.toString()

            if (validateInputFields(name, email)) {
                val subscriber = SubscriberEntity(
                    name =  name,
                    email = email
                )
                insertSubscriber(subscriber)
            }
        }
    }


    private fun insertSubscriber(subscriber: SubscriberEntity) {
        val app = application as App
        val dao = app.database.subscriberDao()

        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(subscriber)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    app.applicationContext,
                    "Assinante inscrito com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            finish()
        }
    }


    private fun validateInputFields(name: String, email: String): Boolean {
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Nome inválido!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) {
            Toast.makeText(this, "Email inválido!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
