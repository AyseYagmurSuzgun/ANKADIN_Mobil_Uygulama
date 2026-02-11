package com.example.ankadin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ankadin.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        binding.btnLoginRedirect.setOnClickListener {
            finish()
        }
    }

    private fun registerUser() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Boş alan kontrolü
        if (name.isEmpty()) {
            binding.tilName.error = "İsim alanı boş bırakılamaz"
            return
        } else {
            binding.tilName.error = null
        }

        if (email.isEmpty()) {
            binding.tilEmail.error = "E-posta alanı boş bırakılamaz"
            return
        } else {
            binding.tilEmail.error = null
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Şifre alanı boş bırakılamaz"
            return
        } else {
            binding.tilPassword.error = null
        }

        // Güçlü şifre kontrolü
        if (!isStrongPassword(password)) {
            binding.tilPassword.error = "Şifre en az 8 karakter, büyük/küçük harf, rakam ve özel karakter içermelidir"
            return
        } else {
            binding.tilPassword.error = null
        }

        setLoadingState(true)

        // Firebase kayıt
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user!!.uid

                val user = User(
                    uid = uid,
                    isim = name,
                    email = email,
                    rol = "user"
                )

                // Firestore kullanıcı kaydı
                firestore.collection("users")
                    .document(uid)
                    .set(user)
                    .addOnSuccessListener {
                        setLoadingState(false)
                        Toast.makeText(this, "Kayıt başarılı! Lütfen giriş yapın.", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        setLoadingState(false)
                        Toast.makeText(this, "Veritabanı hatası: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener { exception ->
                setLoadingState(false)
                val errorMessage = when (exception) {
                    is FirebaseAuthUserCollisionException -> "Bu e-posta adresi zaten kullanımda."
                    is FirebaseAuthWeakPasswordException -> "Şifre çok zayıf."
                    is FirebaseAuthInvalidCredentialsException -> "Geçersiz e-posta formatı."
                    else -> "Kayıt başarısız: ${exception.localizedMessage}"
                }
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun setLoadingState(isLoading: Boolean) {
        if (isLoading) {
            binding.btnRegister.isEnabled = false
            binding.btnRegister.text = "Kayıt Yapılıyor..."
        } else {
            binding.btnRegister.isEnabled = true
            binding.btnRegister.text = "KAYIT OL"
        }
    }

    // Güçlü şifre fonksiyonu
    private fun isStrongPassword(password: String): Boolean {
        val passwordRegex = Regex(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$"
        )
        return passwordRegex.matches(password)
    }
}
