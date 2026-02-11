package com.example.ankadin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.ankadin.databinding.FragmentSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        val sharedPref = requireActivity().getSharedPreferences("ANKADIN_PREFS", Context.MODE_PRIVATE)

        val isDarkMode = sharedPref.getBoolean("darkMode", false)
        binding.switchTheme.isChecked = isDarkMode

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("darkMode", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val notificationsEnabled = sharedPref.getBoolean("notifications", true)
        binding.switchNotifications.isChecked = notificationsEnabled
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("notifications", isChecked).apply()
        }

        val videoEnabled = sharedPref.getBoolean("video_enabled", false)
        binding.switchSiren.isChecked = videoEnabled
        binding.switchSiren.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("video_enabled", isChecked).apply()
            val mesaj = if (isChecked) "SOS anında otomatik kamera açılacak 🎥" else "Video kaydı kapalı"
            Toast.makeText(requireContext(), mesaj, Toast.LENGTH_SHORT).show()
        }

        binding.layoutSosMessage.setOnClickListener {
            showEditSosMessageDialog()
        }

        binding.layoutContactUs.setOnClickListener {
            sendSupportEmail()
        }
    }

    private fun showEditSosMessageDialog() {
        val sharedPref = requireActivity().getSharedPreferences("ANKADIN_PREFS", Context.MODE_PRIVATE)
        val defaultMessage = getString(R.string.default_sos_message)
        val currentMessage = sharedPref.getString("custom_sos_message", defaultMessage)

        val editText = EditText(requireContext()).apply {
            setText(currentMessage)
            setHint("Özel SOS mesajınızı buraya yazın...")
            setPadding(60, 40, 60, 40)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("SOS Mesajını Düzenle")
            .setMessage("Acil durumda gönderilecek mesajı buradan özelleştirebilirsiniz. Konumunuz otomatik olarak eklenecektir.")
            .setView(editText)
            .setNegativeButton("İptal", null)
            .setPositiveButton("Kaydet") { _, _ ->
                val newMessage = editText.text.toString().trim()
                if (newMessage.isNotEmpty()) {
                    sharedPref.edit().putString("custom_sos_message", newMessage).apply()
                    Toast.makeText(requireContext(), "Yeni mesaj kaydedildi!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Mesaj boş olamaz!", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

    private fun sendSupportEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:destek@ankadin.com")
            putExtra(Intent.EXTRA_SUBJECT, "ANKADIN - Destek ve Geri Bildirim")
            putExtra(Intent.EXTRA_TEXT, "Merhaba ANKADIN ekibi,\n\n")
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "E-posta gönderebilecek bir uygulama bulunamadı.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
