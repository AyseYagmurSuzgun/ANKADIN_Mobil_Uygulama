package com.example.ankadin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ankadin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        replaceFragment(HomeFragment())
        updateTitle("Acil Yardım")

        // Alt Menü
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // 1. Sayfa: ACİL YARDIM (HomeFragment - SOS Butonu)
                R.id.nav_emergency -> {
                    replaceFragment(HomeFragment())
                    updateTitle("Acil Yardım")
                }
                // 2. Sayfa: PROFİL (ProfileFragment - Kişisel/Sağlık)
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    updateTitle("Profilim")
                }
                // 3. Sayfa: KİŞİLER (AcilKisilerFragment)
                R.id.nav_contacts -> {
                    replaceFragment(AcilKisilerFragment())
                    updateTitle("Acil Durum Kişileri")
                }
                // 4. Sayfa: HARİTA (AcilDurumFragment - Harita/Konum)
                R.id.nav_map -> {
                    replaceFragment(AcilDurumFragment())
                    updateTitle("Harita ve Konum")
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun updateTitle(title: String) {
        val titleView = findViewById<TextView>(R.id.toolbarTitle)
        titleView?.text = title
    }

    // Options Menü
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_about -> {
                replaceFragment(AboutFragment())
                updateTitle("Hakkında")
                true
            }
            R.id.menu_settings -> {
                replaceFragment(SettingsFragment())
                updateTitle("Ayarlar")
                true
            }
            R.id.menu_logout -> {
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
