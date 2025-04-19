package com.arquitectura.gasolineraapp.vista.sucursal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.arquitectura.gasolineraapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class vMapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapa: GoogleMap
    private lateinit var btnConfirmar: Button
    private var ubicacionSeleccionada: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_mapa)

        btnConfirmar = findViewById(R.id.btnConfirmar)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btnConfirmar.setOnClickListener {
            ubicacionSeleccionada?.let {
                val intent = Intent()
                intent.putExtra("latitud", it.latitude)
                intent.putExtra("longitud", it.longitude)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapa = googleMap
        val bolivia = LatLng(-17.7833, -63.1821)
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(bolivia, 12f))

        mapa.setOnMapLongClickListener {
            mapa.clear()
            mapa.addMarker(MarkerOptions().position(it).title("Ubicación seleccionada"))
            ubicacionSeleccionada = it
        }
    }
}