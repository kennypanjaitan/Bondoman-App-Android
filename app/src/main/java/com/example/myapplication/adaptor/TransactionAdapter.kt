package com.example.myapplication


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.Transaction

class TransactionAdapter (private val transactionList:ArrayList<Transaction>)
    : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.transactionTitle)
        val date : TextView = itemView.findViewById(R.id.transactionDate)
        val loc : TextView = itemView.findViewById(R.id.transactionLoc)
        val nominal : TextView = itemView.findViewById(R.id.transactionNominal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_card, parent,false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.title.text = transaction.title
        holder.date.text = transaction.date
        holder.loc.text = transaction.loc
        holder.nominal.text = "RP.${transaction.nominal}"

    }

}