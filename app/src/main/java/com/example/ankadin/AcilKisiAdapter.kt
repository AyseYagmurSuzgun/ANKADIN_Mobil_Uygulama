package com.example.ankadin

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ankadin.data.AcilKisiEntity
import com.example.ankadin.databinding.ItemAcilKisiBinding // ViewBinding sınıfını import ediyoruz

class AcilKisiAdapter(
    private val liste: List<AcilKisiEntity>,
    private val onItemClick: (AcilKisiEntity) -> Unit
) : RecyclerView.Adapter<AcilKisiAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAcilKisiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAcilKisiBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kisi = liste[position]

        holder.binding.tvAd.text = kisi.isim
        holder.binding.tvTelefon.text = kisi.telefon

        try {
            if (!kisi.resimUri.isNullOrEmpty()) {
                holder.binding.imgKisiFoto.setImageURI(Uri.parse(kisi.resimUri))
            } else {
                holder.binding.imgKisiFoto.setImageResource(R.drawable.ic_profile)
            }
        } catch (e: Exception) {
            holder.binding.imgKisiFoto.setImageResource(R.drawable.ic_profile)
        }

        holder.itemView.setOnClickListener {
            onItemClick(kisi)
        }
    }

    override fun getItemCount(): Int = liste.size
}
