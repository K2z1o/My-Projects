package com.example.ereport.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ereport.R
import com.example.ereport.model.Ostrzezenie
import com.example.ereport.model.StatusOstrzezenia
class OstrzezenieAdapter(
    private val items: MutableList<Ostrzezenie>,
    private val onDelete: (Ostrzezenie) -> Unit,
    private val onClick: (Ostrzezenie) -> Unit
) : RecyclerView.Adapter<OstrzezenieAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tytul: TextView = view.findViewById(R.id.tytulText)
        val grupa: TextView = view.findViewById(R.id.grupaText)
        val czas: TextView = view.findViewById(R.id.czasText)
        val opis: TextView = view.findViewById(R.id.opisText)
        val statusBar: View = view.findViewById(R.id.statusBar)
        val deleteBtn: ImageButton = view.findViewById(R.id.deleteButton)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        android.util.Log.d("ADAPTER", "tytul=${item.Tytul}, start=${item.CzasRozpoczecia}, status=${item.getStatus()}")


        holder.tytul.text = item.Tytul
        holder.grupa.text = item.Grupa
        holder.czas.text = item.formatCzas()
        holder.opis.visibility = View.GONE

        val color = when (item.getStatus()) {
            StatusOstrzezenia.NADCHODZACE -> ContextCompat.getColor(holder.itemView.context, R.color.orange)
            StatusOstrzezenia.AKTYWNE     -> ContextCompat.getColor(holder.itemView.context, R.color.red)
            StatusOstrzezenia.ZAKONCZONE  -> ContextCompat.getColor(holder.itemView.context, R.color.green)
        }
        holder.statusBar.setBackgroundColor(color)

        holder.itemView.setOnClickListener {
            holder.opis.visibility =
                if (holder.opis.visibility == View.GONE) View.VISIBLE else View.GONE
            onClick(item)
        }

        holder.deleteBtn.setOnClickListener {
            onDelete(item)
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ostrzezenie, parent, false)
        return ViewHolder(view)
    }

    fun updateItems(newItems: List<Ostrzezenie>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
