package com.example.ankadin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ankadin.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email ve şifre boş olamaz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    val uid = auth.currentUser?.uid

                    if (uid == null) {
                        Toast.makeText(this, "UID alınamadı", Toast.LENGTH_LONG).show()
                        return@addOnSuccessListener
                    }

                    firestore.collection("users")
                        .document(uid)
                        .get()
                        .addOnSuccessListener { document ->

                            if (!document.exists()) {
                                Toast.makeText(
                                    this,
                                    "Kullanıcı Firestore'da bulunamadı",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@addOnSuccessListener
                            }

                            val user = document.toObject(User::class.java)

                            if (user == null) {
                                Toast.makeText(
                                    this,
                                    "Kullanıcı verisi okunamadı",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@addOnSuccessListener
                            }

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Firestore okuma hatası: ${e.localizedMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this,
                        "Giriş başarısız: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
