package com.example.ankadin

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ankadin.databinding.FragmentAcilDurumBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class AcilDurumFragment : Fragment(R.layout.fragment_acil_durum), OnMapReadyCallback {

    private var _binding: FragmentAcilDurumBinding? = null
    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null

    // Manuel konum
    private val manualCurrentLocation = LatLng(38.3301841, 38.4474331)

    // Diğer manuel konumlar (Hastane, Karakol)
    private val hastaneKonum = LatLng(38.3379388, 38.4277135)
    private val karakolKonum = LatLng(38.334249, 38.4363341)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAcilDurumBinding.bind(view)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        setupButtons()
    }

    private fun setupButtons() {
        binding.fabMyLocation.setOnClickListener {
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(manualCurrentLocation, 15f))
            Toast.makeText(requireContext(), "Manuel konuma odaklandı", Toast.LENGTH_SHORT).show()
        }

        binding.fabMapType.setOnClickListener {
            googleMap?.let { map ->
                if (map.mapType == GoogleMap.MAP_TYPE_NORMAL) {
                    map.mapType = GoogleMap.MAP_TYPE_HYBRID
                } else {
                    map.mapType = GoogleMap.MAP_TYPE_NORMAL
                }
            }
        }

        binding.btnKarakolAra.setOnClickListener {
            showNearbyPlaces("police", "Belirlenen Karakol")
        }

        binding.btnHastaneAra.setOnClickListener {
            showNearbyPlaces("hospital", "Belirlenen Hastane")
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Zoom butonları (+/-)
        googleMap?.uiSettings?.isZoomControlsEnabled = true
        googleMap?.uiSettings?.isCompassEnabled = true

        // Yol tarifi alma
        googleMap?.setOnInfoWindowClickListener { marker ->
            if (marker.title != "Konumum (Manuel)") {
                openGoogleMapsNavigation(marker.position.latitude, marker.position.longitude)
            }
        }

        loadManualLocation()
    }

    private fun loadManualLocation() {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(manualCurrentLocation, 15f))

        googleMap?.addMarker(
            MarkerOptions()
                .position(manualCurrentLocation)
                .title("Konumum (Manuel)")
        )

        getAddressFromLocation(manualCurrentLocation.latitude, manualCurrentLocation.longitude)
    }

    private fun getAddressFromLocation(lat: Double, lng: Double) {
        try {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(lat, lng, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val fullAddress = "${address.thoroughfare ?: ""}, ${address.subLocality ?: ""}, ${address.subAdminArea ?: ""}"

                binding.tvCurrentAddress.text = if (fullAddress.trim().replace(",", "").isEmpty()) {
                    "Adres detayı bulunamadı"
                } else {
                    fullAddress
                }
            } else {
                binding.tvCurrentAddress.text = "Bilinmeyen Adres"
            }
        } catch (e: Exception) {
            binding.tvCurrentAddress.text = "Adres çözümlenemedi (İnternet gerekebilir)"
            e.printStackTrace()
        }
    }

    private fun showNearbyPlaces(type: String, title: String) {
        googleMap?.clear()

        googleMap?.addMarker(MarkerOptions().position(manualCurrentLocation).title("Konumum (Manuel)"))

        Toast.makeText(requireContext(), "$title gösteriliyor. Yol tarifi için pine tıklayın.", Toast.LENGTH_LONG).show()

        var targetLocation: LatLng? = null

        if (type == "hospital") {
            targetLocation = hastaneKonum
            googleMap?.addMarker(MarkerOptions()
                .position(hastaneKonum)
                .title("Devlet Hastanesi")
                .snippet("Yol tarifi için tıklayın")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))

        } else if (type == "police") {
            targetLocation = karakolKonum
            googleMap?.addMarker(MarkerOptions()
                .position(karakolKonum)
                .title("Merkez Karakolu")
                .snippet("Yol tarifi için tıklayın")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
        }

        if (targetLocation != null) {
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, 14f))
        }
    }

    private fun openGoogleMapsNavigation(destLat: Double, destLng: Double) {
        val uri = "http://maps.google.com/maps?saddr=${manualCurrentLocation.latitude},${manualCurrentLocation.longitude}&daddr=$destLat,$destLng"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        intent.setPackage("com.google.android.apps.maps")

        try {
            startActivity(intent)
        } catch (e: Exception) {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(webIntent)
        }
    }

    override fun onResume() { super.onResume(); binding.mapView.onResume() }
    override fun onPause() { super.onPause(); binding.mapView.onPause() }
    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
        _binding = null
    }
    override fun onLowMemory() { super.onLowMemory(); binding.mapView.onLowMemory() }
}
