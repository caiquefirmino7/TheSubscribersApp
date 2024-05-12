package com.caique.thesubscribrerapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.caique.thesubscribrerapp.data.entity.SubscriberEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class EditSubscriber : AppCompatActivity() {

    private val subscriberId: Int by lazy { intent.getIntExtra("subscriber_id", -1) }
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var subscriber: SubscriberEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_subscriber)
        val  editSaveButton = findViewById<Button>(R.id.buttonEdit)
        initSetup()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                subscriber = getSubscriberById(subscriberId)
                dataSubscriber()
            } catch (e: Exception) {
                Toast.makeText(this@EditSubscriber, "Usuário nao encontrado!", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }

        editSaveButton.setOnClickListener {
            subscriber.name = nameEditText.text.toString()
            subscriber.email = emailEditText.text.toString()

            CoroutineScope(Dispatchers.Main).launch {
                saveEditSubscriber(subscriber)
            }
        }
    }

    private fun initSetup() {
        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmail)

    }

    private fun dataSubscriber() {
        nameEditText.setText(subscriber.name)
        emailEditText.setText(subscriber.email)
    }

    private suspend fun getSubscriberById(subscriberId: Int): SubscriberEntity {
        return withContext(Dispatchers.IO) {
            val app = application as App
            val dao = app.database.subscriberDao()
            dao.getSubscriberById(subscriberId)
                ?: throw IllegalStateException("Usuário nao encontrado!")
        }

    }
    private suspend fun saveEditSubscriber(subscriber: SubscriberEntity) {
        try {
            val app = application as App
            val dao = app.database.subscriberDao()
            withContext(Dispatchers.IO) {
                dao.update(subscriber)
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@EditSubscriber,
                    "assinante atualizado com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        } catch (e: Exception) {
            Log.e("TAG", "Erro: ${e.message}", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@EditSubscriber,
                    "Erro ao atualizar detalhes do assinante!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}