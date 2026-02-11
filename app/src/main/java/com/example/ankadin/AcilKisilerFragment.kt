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
import com.example.ankadin.data.AcilKisiEntity
import com.example.ankadin.data.database.AppDatabase
import com.example.ankadin.databinding.FragmentAcilKisilerBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AcilKisilerFragment : Fragment(R.layout.fragment_acil_kisiler) {

    private var _binding: FragmentAcilKisilerBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase
    private var secilenFotoUri: Uri? = null

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
                requireContext().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            secilenFotoUri = it
            binding.imgFoto.setImageURI(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAcilKisilerBinding.bind(view)
        db = AppDatabase.getDatabase(requireContext())

        binding.rvAcilKisiler.layoutManager = LinearLayoutManager(requireContext())
        listeyiYukle()

        binding.btnFotoSec.setOnClickListener { checkPermissionAndPickImage() }
        binding.btnKaydet.setOnClickListener { kaydetIslemi() }
    }

    private fun listeyiYukle() {
        lifecycleScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val liste = db.acilKisiDao().getAll(uid)

            val adapter = AcilKisiAdapter(liste) { tiklananKisi ->
                val kisiId = tiklananKisi.id
                val bundle = Bundle().apply {
                    putInt("kisiId", kisiId)
                }
                val detayFragment = AcilKisiDetayFragment()
                detayFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, detayFragment)
                    .addToBackStack(null)
                    .commit()
            }
            binding.rvAcilKisiler.adapter = adapter
        }
    }

    private fun kaydetIslemi() {
        val ad = binding.etAd.text.toString().trim()
        val tel = binding.etTelefon.text.toString().trim()

        if (ad.isEmpty() || tel.isEmpty() || secilenFotoUri == null) {
            Toast.makeText(requireContext(), "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val sayi = db.acilKisiDao().count(uid)
            if (sayi >= 3) {
                Toast.makeText(requireContext(), "En fazla 3 acil kişi eklenebilir", Toast.LENGTH_SHORT).show()
            } else {
                db.acilKisiDao().insert(
                    AcilKisiEntity(
                        uid = uid,
                        isim = ad,
                        telefon = tel,
                        resimUri = secilenFotoUri.toString()
                    )
                )
                Toast.makeText(requireContext(), "Acil kişi eklendi", Toast.LENGTH_SHORT).show()
                temizle()
                listeyiYukle()
            }
        }
    }

    private fun temizle() {
        binding.etAd.text?.clear()
        binding.etTelefon.text?.clear()
        binding.imgFoto.setImageResource(R.drawable.ic_profile)
        secilenFotoUri = null
    }

    private fun checkPermissionAndPickImage() {
        val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(requireContext(), permissionToRequest) == PackageManager.PERMISSION_GRANTED -> {
                fotoSecLauncher.launch(arrayOf("image/*"))
            }
            else -> {
                requestStoragePermissionLauncher.launch(permissionToRequest)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
