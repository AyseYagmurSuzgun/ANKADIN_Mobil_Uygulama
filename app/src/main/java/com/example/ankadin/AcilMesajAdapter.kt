package com.example.ankadin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ankadin.data.AcilMesajEntity
import com.example.ankadin.databinding.ItemAcilMesajBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AcilMesajAdapter(
    private val mesajListesi: List<AcilMesajEntity>,
    private val onItemClicked: (AcilMesajEntity) -> Unit
) : RecyclerView.Adapter<AcilMesajAdapter.MesajViewHolder>() {

    inner class MesajViewHolder(val binding: ItemAcilMesajBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesajViewHolder {
        val binding = ItemAcilMesajBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MesajViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MesajViewHolder, position: Int) {
        val mevcutMesaj = mesajListesi[position]
        holder.binding.tvMesaj.text = mevcutMesaj.mesaj

        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val tarihString = sdf.format(Date(mevcutMesaj.tarih))
        holder.binding.tvTarih.text = tarihString

        holder.itemView.setOnClickListener {
            onItemClicked(mevcutMesaj)
        }
    }

    override fun getItemCount(): Int = mesajListesi.size
}

