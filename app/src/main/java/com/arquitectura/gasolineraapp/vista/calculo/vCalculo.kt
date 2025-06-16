package com.arquitectura.gasolineraapp.vista.calculo

import android.app.Activity
import android.widget.*
import com.arquitectura.gasolineraapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*

class vCalculo(private val activity: Activity) : OnMapReadyCallback {

    private val mapView: MapView = activity.findViewById(R.id.mapView)
    private val btnMarcar: Button = activity.findViewById(R.id.btnMarcar)
    private val btnLimpiar: Button = activity.findViewById(R.id.btnLimpiar)
    private val btnCalcular: Button = activity.findViewById(R.id.btnCalcular)
    private val txtResultado: TextView = activity.findViewById(R.id.txtResultado)
    private val btnAtras: ImageButton = activity.findViewById(R.id.btnAtras)
    private val spinnerModo: Spinner = activity.findViewById(R.id.spinnerModo)

    private var mapa: GoogleMap? = null
    private var marcadorSucursal: Marker? = null
    private var linea: Polyline? = null
    private val puntosRuta = mutableListOf<LatLng>()
    private var sucursalLatLng: LatLng? = null

    init {
        mapView.onCreate(null)
        mapView.onResume()
        mapView.getMapAsync(this)

        // Configurar el Spinner con el array de recursos
        val adapter = ArrayAdapter.createFromResource(
            activity,
            R.array.modos_estimacion,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerModo.adapter = adapter
    }

    override fun onMapReady(map: GoogleMap) {
        mapa = map
        sucursalLatLng?.let {
            marcadorSucursal = mapa?.addMarker(MarkerOptions().position(it).title("Sucursal"))
            mapa?.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 17f))
        }
    }

    // ==== Getters y Setters ====

    fun setUbicacionSucursal(latLng: LatLng) {
        sucursalLatLng = latLng
    }

    fun getPuntosRuta(): List<LatLng> {
        return puntosRuta.toList()
    }

    fun getModoSeleccionado(): String {
        return spinnerModo.selectedItem.toString()
    }

    // ==== Acciones ====

    fun marcarPuntoEnCentro() {
        val centro = mapa?.cameraPosition?.target ?: return
        puntosRuta.add(centro)
        redibujarRuta()
    }

    fun limpiarRuta() {
        puntosRuta.clear()
        linea?.remove()
        txtResultado.text = ""
    }

    fun mostrarResultado(tiempo: Double, alcanza: Boolean) {
        txtResultado.text = if (alcanza) {
            "✅ Alcanzará el combustible.\nTiempo estimado: %.2f minutos.".format(tiempo)
        } else {
            "❌ No alcanzará el combustible."
        }
    }

    fun mostrarError(mensaje: String) {
        txtResultado.text = mensaje
    }

    // ==== Redibujar línea en el mapa ====

    private fun redibujarRuta() {
        linea?.remove()
        linea = mapa?.addPolyline(
            PolylineOptions()
                .addAll(puntosRuta)
                .color(0xFF6200EE.toInt())
                .width(8f)
        )
    }

    // ==== Callbacks de botones ====

    fun onBtnMarcarClick(callback: () -> Unit) {
        btnMarcar.setOnClickListener { callback() }
    }

    fun onBtnLimpiarClick(callback: () -> Unit) {
        btnLimpiar.setOnClickListener { callback() }
    }

    fun onBtnCalcularClick(callback: () -> Unit) {
        btnCalcular.setOnClickListener { callback() }
    }

    fun onBtnAtrasClick(callback: () -> Unit) {
        btnAtras.setOnClickListener { callback() }
    }
}
