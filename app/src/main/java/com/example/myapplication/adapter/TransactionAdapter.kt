package com.example.myapplication.adapter


import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.CategoryEnum
import com.example.myapplication.room.TransactionEntity

class TransactionAdapter(
    private val transactionList: ArrayList<TransactionEntity>,
    private val listener: OnAdapterListener
)
    : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.transactionTitle)
        val date : TextView = itemView.findViewById(R.id.transactionDate)
        val loc : TextView = itemView.findViewById(R.id.transactionLoc)
        val nominal : TextView = itemView.findViewById(R.id.transactionNominal)
        val detailbtn : Button = itemView.findViewById(R.id.transactionDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_card, parent,false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    fun setData(list: List<TransactionEntity>) {
        transactionList.clear()
        transactionList.addAll(list)
        notifyDataSetChanged()
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.title.text = transaction.title
        holder.date.text = transaction.date
        holder.loc.text = transaction.location
        if (transaction.category == CategoryEnum.INCOME) {
            holder.nominal.setTextColor(Color.GREEN)
            holder.nominal.text = "+ RP.${transaction.nominal}"
        }
        else if (transaction.category == CategoryEnum.EXPENSE) {
            holder.nominal.setTextColor(Color.RED)
            holder.nominal.text = "- RP.${transaction.nominal}"
        }
        holder.detailbtn.setOnClickListener {
            listener.onClick(transaction)
        }

    }

    interface OnAdapterListener {
        fun onClick(transaction: TransactionEntity)
    }

}