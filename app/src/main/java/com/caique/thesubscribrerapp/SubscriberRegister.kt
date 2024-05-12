package com.caique.thesubscribrerapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.caique.thesubscribrerapp.data.entity.SubscriberEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubscriberRegister : AppCompatActivity() {

    private lateinit var nameLogin: EditText
    private lateinit var emailLogin: EditText
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscriber_register)

        initViews()
        btnSubmit()


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
                    "Usuário inscrito com sucesso!",
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
