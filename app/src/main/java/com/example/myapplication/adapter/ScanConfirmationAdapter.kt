package com.example.myapplication


import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.models.CategoryEnum
import com.example.myapplication.room.TransactionEntity

class ScanConfirmationAdapter(
    private val transactionList: ArrayList<TransactionEntity>,
)
    : RecyclerView.Adapter<ScanConfirmationAdapter.ScanViewHolder>() {
    class ScanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.transactionTitle)
        val nominal : TextView = itemView.findViewById(R.id.transactionNominal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scan_card, parent,false)
        return ScanViewHolder(view)
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
    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.title.text = transaction.title
        holder.nominal.text = transaction.nominal.toString()

    }


}