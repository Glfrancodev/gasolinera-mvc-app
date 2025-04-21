// Ruta: com/arquitectura/gasolineraapp/vista/calculo/vCalculo.kt
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

    val mapView: MapView = activity.findViewById(R.id.mapView)
    val btnMarcar: Button = activity.findViewById(R.id.btnMarcar)
    val btnLimpiar: Button = activity.findViewById(R.id.btnLimpiar)
    val btnCalcular: Button = activity.findViewById(R.id.btnCalcular)
    val txtResultado: TextView = activity.findViewById(R.id.txtResultado)
    val btnAtras: ImageButton = activity.findViewById(R.id.btnAtras)

    var mapa: GoogleMap? = null
    var marcadorSucursal: Marker? = null
    val puntosRuta = mutableListOf<LatLng>()
    var linea: Polyline? = null
    var sucursalLatLng: LatLng? = null

    init {
        mapView.onCreate(null)
        mapView.onResume()
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        mapa = map
        sucursalLatLng?.let {
            marcadorSucursal = mapa?.addMarker(MarkerOptions().position(it).title("Sucursal"))
            mapa?.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 17f))
        }
    }

    fun marcarPuntoEnCentro() {
        val centro = mapa?.cameraPosition?.target ?: return
        puntosRuta.add(centro)
        redibujarRuta()
    }

    private fun redibujarRuta() {
        linea?.remove()
        linea = mapa?.addPolyline(
            PolylineOptions()
                .addAll(puntosRuta)
                .color(0xFF6200EE.toInt())
                .width(8f)
        )
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
}
