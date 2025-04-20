package com.arquitectura.gasolineraapp.vista.calculo

import android.app.Activity
import android.widget.Button
import android.widget.TextView
import com.arquitectura.gasolineraapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class vCalculoActivity(activity: Activity) : OnMapReadyCallback {

    private val mapView: MapView = activity.findViewById(R.id.mapView)
    private val btnDibujar: Button = activity.findViewById(R.id.btnDibujar)
    private val btnCalcular: Button = activity.findViewById(R.id.btnCalcular)
    private val txtResultado: TextView = activity.findViewById(R.id.txtResultado)
    private var mapa: GoogleMap? = null
    private var onDibujar: (() -> Unit)? = null
    private var onCalcular: (() -> Unit)? = null

    // Variables para almacenar ubicación si se llama antes de que el mapa esté listo
    private var marcadorLat: Double? = null
    private var marcadorLng: Double? = null

    init {
        mapView.onCreate(null)
        mapView.onResume() // ← importante para que el mapa se muestre
        mapView.getMapAsync(this)
    }

    fun mostrarMapaEnUbicacion(lat: Double, lon: Double) {
        marcadorLat = lat
        marcadorLng = lon

        if (mapa != null) {
            val ubicacion = LatLng(lat, lon)
            mapa?.addMarker(MarkerOptions().position(ubicacion).title("Sucursal"))
            mapa?.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 17f))
        }
    }

    fun setOnDibujarClick(callback: () -> Unit) {
        onDibujar = callback
        btnDibujar.setOnClickListener { callback() }
    }

    fun setOnCalcularClick(callback: () -> Unit) {
        onCalcular = callback
        btnCalcular.setOnClickListener { callback() }
    }

    fun abrirMapaParaDibujar(callbackResultado: (Double) -> Unit) {
        // Aquí se lanza otra Activity para dibujar la fila (implementación futura)
        callbackResultado(75.0) // ← simulado mientras no tengamos Polyline real
    }

    fun mostrarResultado(tiempo: Double, alcanza: Boolean) {
        txtResultado.text = if (alcanza) {
            "✅ Alcanzará el combustible.\nTiempo estimado: %.2f minutos.".format(tiempo)
        } else {
            "❌ No alcanzará el combustible."
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mapa = map

        // Mostrar marcador si ya se pidió ubicación antes de que el mapa esté listo
        marcadorLat?.let { lat ->
            marcadorLng?.let { lon ->
                val ubicacion = LatLng(lat, lon)
                mapa?.addMarker(MarkerOptions().position(ubicacion).title("Sucursal"))
                mapa?.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 17f))
            }
        }
    }
}
