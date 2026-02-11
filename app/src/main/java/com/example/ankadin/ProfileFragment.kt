package com.example.ankadin

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.ankadin.data.UserProfileEntity
import com.example.ankadin.data.database.AppDatabase
import com.example.ankadin.databinding.FragmentProfileBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProfileBinding.bind(view)
        database = AppDatabase.getDatabase(requireContext())

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Oturum açılmamış!", Toast.LENGTH_SHORT).show()
            return
        }
        val uid = currentUser.uid

        setupBloodTypeDropdown()

        binding.etDogumTarihi.setOnClickListener { showDatePicker() }

        binding.etDogumTarihi.parent.let { parent ->
            if (parent is View) parent.setOnClickListener { showDatePicker() }
        }

        binding.btnSave.setOnClickListener {
            val profile = UserProfileEntity(
                uid = uid,
                isim = binding.etName.text.toString(),
                kimlikNo = binding.etKimlik.text.toString(),
                cinsiyet = binding.etCinsiyet.text.toString(),
                yas = binding.etYas.text.toString().toIntOrNull(),
                dogumTarihi = binding.etDogumTarihi.text.toString(),
                telefon = binding.etTelefon.text.toString(),
                kanGrubu = binding.etKanGrubu.text.toString(),
                alerjiler = binding.etAlerji.text.toString(),
                ilaclar = binding.etIlac.text.toString(),
                hastaliklar = binding.etHastalik.text.toString()
            )

            lifecycleScope.launch {
                try {
                    database.userProfileDao().insertProfile(profile)
                    Toast.makeText(requireContext(), "Bilgiler başarıyla kaydedildi ✅", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            val profile = database.userProfileDao().getProfile(uid)
            profile?.let {
                binding.etName.setText(it.isim)
                binding.etKimlik.setText(it.kimlikNo)
                binding.etCinsiyet.setText(it.cinsiyet)
                binding.etYas.setText(it.yas?.toString() ?: "")
                binding.etDogumTarihi.setText(it.dogumTarihi)
                binding.etTelefon.setText(it.telefon)
                binding.etKanGrubu.setText(it.kanGrubu, false)
                binding.etAlerji.setText(it.alerjiler)
                binding.etIlac.setText(it.ilaclar)
                binding.etHastalik.setText(it.hastaliklar)
            }
        }
    }

    private fun setupBloodTypeDropdown() {
        val kanGruplari = listOf(
            "A Rh+", "A Rh-",
            "B Rh+", "B Rh-",
            "AB Rh+", "AB Rh-",
            "0 Rh+", "0 Rh-"
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, kanGruplari)
        binding.etKanGrubu.setAdapter(adapter)
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Doğum Tarihinizi Seçin")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTheme(R.style.CustomMaterialCalendar)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateString = sdf.format(Date(selection))
            binding.etDogumTarihi.setText(dateString)
        }

        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
