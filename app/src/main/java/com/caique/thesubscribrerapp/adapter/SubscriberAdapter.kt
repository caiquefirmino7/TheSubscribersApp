package com.caique.thesubscribrerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.caique.thesubscribrerapp.R
import com.caique.thesubscribrerapp.data.entity.SubscriberEntity

class SubscriberAdapter(
    private val subscriberList: MutableList<SubscriberEntity>,
    private val onItemLongClickListener:(Int) -> Unit
) : RecyclerView.Adapter<SubscriberAdapter.SubscriberViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.subscriber_item, parent, false)
        return SubscriberViewHolder(view)

    }

    override fun getItemCount(): Int {
        return subscriberList.size
    }

    override fun onBindViewHolder(holder: SubscriberViewHolder, position: Int) {
        val currentItem = subscriberList[position]
        holder.bind(currentItem)

        holder.itemView.setOnLongClickListener {
            onItemLongClickListener.invoke(position)
            true // Indicando que o evento de clique longo foi consumido
        }
    }


    class SubscriberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val emailTextView: TextView = itemView.findViewById(R.id.textViewEmail)

        fun bind(currentItem: SubscriberEntity) {
            nameTextView.text = currentItem.name
            emailTextView.text = currentItem.email
        }

    }

}