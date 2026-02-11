package com.example.ankadin

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import com.example.ankadin.databinding.FragmentAboutBinding

class AboutFragment : Fragment(R.layout.fragment_about) {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAboutBinding.bind(view)

        setupVersionInfo()

        startEntranceAnimations()
    }

    private fun setupVersionInfo() {
        try {
            val context = requireContext()
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val version = pInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun startEntranceAnimations() {
        // Logo için yukarıdan aşağıya süzülme animasyonu
        val logoAnim = AnimationSet(true).apply {
            addAnimation(AlphaAnimation(0.0f, 1.0f))
            addAnimation(TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -0.5f,
                Animation.RELATIVE_TO_SELF, 0.0f
            ))
            duration = 800
            fillAfter = true
        }

        // Metinler için aşağıdan yukarıya süzülme animasyonu
        val textAnim = AnimationSet(true).apply {
            addAnimation(AlphaAnimation(0.0f, 1.0f))
            addAnimation(TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.2f,
                Animation.RELATIVE_TO_SELF, 0.0f
            ))
            duration = 800
            startOffset = 200
            fillAfter = true
        }

        // Animasyonları bileşenlere uygulama
        binding.imgAboutLogo.startAnimation(logoAnim)
        binding.tvAboutText.startAnimation(textAnim)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
