package com.example.ankadin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ankadin.data.database.AppDatabase
import com.example.ankadin.databinding.FragmentAcilKisiDetayBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class AcilKisiDetayFragment : Fragment(R.layout.fragment_acil_kisi_detay) {

    private var _binding: FragmentAcilKisiDetayBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase
    private var kisiId: Int = -1
    private var mevcutResimUri: Uri? = null

    private val requestStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                fotoSecLauncher.launch(arrayOf("image/*"))
            } else {
                Toast.makeText(requireContext(), "Fotoğraf seçmek için galeri izni reddedildi.", Toast.LENGTH_LONG).show()
            }
        }

    private val fotoSecLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            try {
                requireContext().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mevcutResimUri = it
            binding.imgFoto.setImageURI(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAcilKisiDetayBinding.bind(view)
        db = AppDatabase.getDatabase(requireContext())

        kisiId = arguments?.getInt("kisiId") ?: -1
        if (kisiId == -1) {
            Toast.makeText(requireContext(), "Kişi bilgisi alınamadı!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
            return
        }

        bilgileriYukle()
        mesajGecmisiniYukle()

        binding.imgFoto.setOnClickListener { checkPermissionAndPickImage() }
        binding.btnGuncelle.setOnClickListener { guncelle() }
        binding.btnSil.setOnClickListener { sil() }
    }

    private fun mesajGecmisiniYukle() {
        lifecycleScope.launch {
            try {
                val mesajlar = db.acilMesajDao().getMesajlarByKisiId(kisiId)
                if (mesajlar.isNotEmpty()) {
                    binding.tvMesajBaslik.text = "Geçmiş Bildirimler (${mesajlar.size})"
                    val adapter = AcilMesajAdapter(mesajlar) { tiklananMesaj ->
                        showMessageDialog(tiklananMesaj.mesaj)
                    }
                    binding.rvMesajlar.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvMesajlar.adapter = adapter
                } else {
                    binding.tvMesajBaslik.text = "Bu kişiye henüz bildirim gönderilmemiş"
                }
            } catch (e: Exception) {
                binding.tvMesajBaslik.text = "Geçmiş yüklenemedi (Veritabanı Hatası)"
                e.printStackTrace()
            }
        }
    }

    private fun showMessageDialog(mesaj: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Gönderilen Mesaj Detayı")
            .setMessage(mesaj)
            .setPositiveButton("Kapat", null)
            .show()
    }

    private fun bilgileriYukle() {
        lifecycleScope.launch {
            val kisi = db.acilKisiDao().getById(kisiId)
            kisi?.let {
                binding.etAd.setText(it.isim)
                binding.etTelefon.setText(it.telefon)
                if (!it.resimUri.isNullOrEmpty()) {
                    try {
                        mevcutResimUri = Uri.parse(it.resimUri)
                        binding.imgFoto.setImageURI(mevcutResimUri)
                    } catch (e: Exception) {
                        binding.imgFoto.setImageResource(R.drawable.ic_profile)
                    }
                } else {
                    binding.imgFoto.setImageResource(R.drawable.ic_profile)
                }
            }
        }
    }

    private fun checkPermissionAndPickImage() {
        val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(requireContext(), permissionToRequest) == PackageManager.PERMISSION_GRANTED -> fotoSecLauncher.launch(arrayOf("image/*"))
            else -> requestStoragePermissionLauncher.launch(permissionToRequest)
        }
    }

    private fun guncelle() {
        val yeniAd = binding.etAd.text.toString().trim()
        val yeniTel = binding.etTelefon.text.toString().trim()
        if (yeniAd.isEmpty() || yeniTel.isEmpty()) {
            Toast.makeText(requireContext(), "Alanlar boş olamaz", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val kisi = db.acilKisiDao().getById(kisiId)
            kisi?.let {
                val guncelKisi = it.copy(
                    isim = yeniAd,
                    telefon = yeniTel,
                    resimUri = mevcutResimUri?.toString() ?: it.resimUri
                )
                db.acilKisiDao().update(guncelKisi)
                Toast.makeText(requireContext(), "Kişi güncellendi ✅", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun sil() {
        lifecycleScope.launch {
            val kisi = db.acilKisiDao().getById(kisiId)
            kisi?.let {
                db.acilKisiDao().delete(it)
                Toast.makeText(requireContext(), "Kişi silindi 🗑️", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
