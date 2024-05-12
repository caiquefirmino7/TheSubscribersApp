package com.caique.thesubscribrerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caique.thesubscribrerapp.adapter.SubscriberAdapter
import com.caique.thesubscribrerapp.data.entity.SubscriberEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var subscriberRecyclerView: RecyclerView
    private lateinit var subscriberAdapter: SubscriberAdapter
    private var subscriberList = mutableListOf<SubscriberEntity>()
    private lateinit var imageEmpty: ImageView
    private lateinit var fab: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialização das visualizações
        initViews()

        // Configuração inicial da atividade
        initSetup()

        // Configuração do listener do FloatingActionButton
        setupFabClickListener()
    }

    // chamado quando a atividade se torna visível ao usuário
    override fun onResume() {
        super.onResume()

        // Atualização da lista de assinantes ao retomar a atividade
        subscriberList.clear()
        loadSubscribers()
    }

    // Inicialização das visualizações
    private fun initViews() {
        subscriberRecyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)
        imageEmpty = findViewById(R.id.imageEmpty)
    }

    // Configuração inicial da atividade
    private fun initSetup() {
        subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        subscriberRecyclerView.setHasFixedSize(true)

        // Configuração do adaptador da RecyclerView
        subscriberAdapter = SubscriberAdapter(subscriberList) { position ->
            deleteOrEditSubscriber(position)
        }
        subscriberRecyclerView.adapter = subscriberAdapter
    }

    // Configuração do listener do FloatingActionButton
    private fun setupFabClickListener() {
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, SubscriberRegister::class.java)
            startActivity(intent)
        }
    }

    // Carregamento inicial da lista de assinantes
    @SuppressLint("NotifyDataSetChanged")
    private fun loadSubscribers() {
        CoroutineScope(Dispatchers.IO).launch {
            val app = application as App
            val dao = app.database.subscriberDao()
            val subscribers = dao.getAllSubscribers()

            // Atualização da lista de assinantes na UI
            withContext(Dispatchers.Main) {
                subscriberList.addAll(subscribers)
                subscriberAdapter.notifyDataSetChanged()

                // Atualização da visibilidade da imagem de vazio
                updateEmptyViewVisibility()
            }
        }
    }

    // Exclusão ou edição de um assinante
    private fun deleteOrEditSubscriber(position: Int) {
        val subscriber = subscriberList[position]
        val options = arrayOf("Editar", "Excluir")
        AlertDialog.Builder(this)
            .setTitle("O que você deseja fazer?")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editSubscriber(subscriber)
                    1 -> confirmDelete(subscriber)
                }
            }
            .show()
    }

    // Edição de um assinante
    private fun editSubscriber(subscriber: SubscriberEntity) {
        val intent = Intent(this, EditSubscriber::class.java)
        intent.putExtra("subscriber_id", subscriber.uid)
        startActivity(intent)
    }

    // Confirmação de exclusão de um assinante
    private fun confirmDelete(subscriber: SubscriberEntity) {
        AlertDialog.Builder(this)
            .setTitle("Excluir Assinante")
            .setMessage("Tem certeza de que deseja excluir este assinante?")
            .setPositiveButton("Sim") { dialog, _ ->
                deleteSubscriber(subscriber)
                dialog.dismiss()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        this@MainActivity,
                        "Assinante excluído com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Exclusão de um assinante
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteSubscriber(subscriber: SubscriberEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val app = application as App
            val dao = app.database.subscriberDao()
            dao.delete(subscriber)
            withContext(Dispatchers.Main) {
                subscriberList.remove(subscriber)
                subscriberAdapter.notifyDataSetChanged()

                // Atualização da visibilidade da imagem de vazio
                updateEmptyViewVisibility()
            }
        }
    }

    // Atualização da visibilidade da imagem de vazio
    private fun updateEmptyViewVisibility() {
        if (subscriberList.isEmpty()) {
            imageEmpty.visibility = View.VISIBLE
            subscriberRecyclerView.visibility = View.GONE
        } else {
            imageEmpty.visibility = View.GONE
            subscriberRecyclerView.visibility = View.VISIBLE
        }
    }
}
