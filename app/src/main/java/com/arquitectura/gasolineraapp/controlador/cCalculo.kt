// Ruta: com/arquitectura/gasolineraapp/controlador/cCalculo.kt
package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import com.arquitectura.gasolineraapp.modelo.*
import com.arquitectura.gasolineraapp.vista.calculo.vCalculoActivity
import com.arquitectura.gasolineraapp.vista.disponibilidad.disponibilidadActivity
import com.google.maps.android.SphericalUtil

class cCalculo(private val activity: Activity) {

    private val vista = vCalculoActivity(activity)
    private val modeloSucursal = mSucursal()
    private val modeloRelacion = mSucursalCombustible()
    private val modeloConstantes = mConstante()

    fun iniciar() {
        val idSucursal = activity.intent.getIntExtra("idSucursal", -1)
        val idSucursalCombustible = activity.intent.getIntExtra("idSucursalCombustible", -1)

        val sucursal = modeloSucursal.listar(activity).find { it.id == idSucursal }
        val relacion = modeloRelacion.listar(activity).find { it.id == idSucursalCombustible }
        val constantes = modeloConstantes.listar(activity)

        if (sucursal == null || relacion == null || constantes.size < 3) {
            vista.txtResultado.text = "Error cargando datos."
            return
        }

        vista.sucursalLatLng = com.google.android.gms.maps.model.LatLng(sucursal.latitud, sucursal.longitud)

        vista.btnMarcar.setOnClickListener {
            vista.marcarPuntoEnCentro()
        }

        vista.btnLimpiar.setOnClickListener {
            vista.limpiarRuta()
        }

        vista.btnCalcular.setOnClickListener {
            if (vista.puntosRuta.size < 2) {
                vista.txtResultado.text = "Trazá al menos 2 puntos."
                return@setOnClickListener
            }

            val distanciaMetros = SphericalUtil.computeLength(vista.puntosRuta)
            val largoAuto = constantes[2].valor
            val litrosPorAuto = constantes[1].valor
            val tiempoCarga = constantes[0].valor
            val autosEnFila = distanciaMetros / largoAuto
            val litrosNecesarios = autosEnFila * litrosPorAuto
            val alcanza = relacion.combustibleDisponible >= litrosNecesarios
            val tiempoTotal = (autosEnFila * tiempoCarga) / relacion.cantidadBombas

            vista.mostrarResultado(tiempoTotal, alcanza)
        }

        vista.btnAtras.setOnClickListener {
            val intent = Intent(activity, disponibilidadActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }
}
