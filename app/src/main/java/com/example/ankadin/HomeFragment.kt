package com.example.ankadin

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.ankadin.data.AcilMesajEntity
import com.example.ankadin.data.database.AppDatabase
import com.example.ankadin.databinding.FragmentHomeBinding
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase
    private val manualCurrentLocation = LatLng(38.3301841, 38.4474331)

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Video kaydı için kamera izni reddedildi.", Toast.LENGTH_LONG).show()
            }
        }

    private val requestSmsPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                sendAcilMesaj(manualCurrentLocation)
            } else {
                Toast.makeText(requireContext(), "Acil mesaj için SMS izni reddedildi.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        db = AppDatabase.getDatabase(requireContext())
        loadManualLocation()

        binding.btnAcilYardim.setOnClickListener {
            Toast.makeText(requireContext(), "Acil durum bildiriliyor...", Toast.LENGTH_SHORT).show()
            sendAcilMesaj(manualCurrentLocation)
        }
    }

    private fun loadManualLocation() {
        binding.tvKonumDetay.text = "38.3301841, 38.4474331"
    }

    private fun sendAcilMesaj(latLng: LatLng) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
            return
        }

        lifecycleScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val contacts = db.acilKisiDao().getAll(uid)
            if (contacts.isEmpty()) {
                Toast.makeText(requireContext(), "Acil durum kişisi eklemelisiniz!", Toast.LENGTH_LONG).show()
                return@launch
            }

            val sharedPref = requireActivity().getSharedPreferences("ANKADIN_PREFS", Context.MODE_PRIVATE)
            val defaultMessage = getString(R.string.default_sos_message)
            val customMessage = sharedPref.getString("custom_sos_message", defaultMessage) ?: defaultMessage
            val profile = db.userProfileDao().getProfile(uid)
            val profilBilgi = profile?.let { "GÖNDERENİN BİLGİLERİ:\nAd: ${it.isim ?: "Bilinmiyor"}\nYaş: ${it.yas?.toString() ?: "Bilinmiyor"}\nKan Grubu: ${it.kanGrubu ?: "Bilinmiyor"}\nBilinen Hastalıklar: ${it.hastaliklar ?: "Yok"}\nKullandığı İlaçlar: ${it.ilaclar ?: "Yok"}\nAlerjiler: ${it.alerjiler ?: "Yok"}" } ?: "Kullanıcı profil bilgisi bulunamadı."
            val mapLink = "https://maps.google.com/?q=${latLng.latitude},${latLng.longitude}"
            val mesaj = "$customMessage\n\nKonumum:\n$mapLink\n\n$profilBilgi"

            var gonderilenSayisi = 0
            for (kisi in contacts) {
                try {
                    SmsManager.getDefault().sendTextMessage(kisi.telefon, null, mesaj, null, null)
                    db.acilMesajDao().insert(AcilMesajEntity(kisiId = kisi.id, mesaj = mesaj))
                    gonderilenSayisi++
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (gonderilenSayisi > 0) {
                Toast.makeText(requireContext(), "$gonderilenSayisi kişiye acil durum mesajı gönderildi!", Toast.LENGTH_LONG).show()
                startVideoRecording()

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, AcilDurumFragment())
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "Mesaj gönderilemedi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startVideoRecording() {
        val sharedPref = requireActivity().getSharedPreferences("ANKADIN_PREFS", Context.MODE_PRIVATE)
        val videoAcik = sharedPref.getBoolean("video_enabled", false)
        if (videoAcik) {
            when {
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> openCamera()
                else -> requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        try {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Kamera uygulaması başlatılamadı.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
