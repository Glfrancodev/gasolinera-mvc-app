package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import com.arquitectura.gasolineraapp.modelo.*
import com.arquitectura.gasolineraapp.vista.calculo.vCalculo
import com.arquitectura.gasolineraapp.vista.disponibilidad.disponibilidadActivity
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

class cCalculo(private val activity: Activity) {

    private val vista = vCalculo(activity)
    private val modeloSucursal = mSucursal()
    private val modeloRelacion = mSucursalCombustible()
    private val modeloVariables = mVariable()

    private var relacionActual: mSucursalCombustible? = null
    private var variables: List<mVariable> = emptyList()

    fun iniciar(): Unit {
        val idSucursal = activity.intent.getIntExtra("idSucursal", -1)
        val idRelacion = activity.intent.getIntExtra("idSucursalCombustible", -1)

        val sucursal = modeloSucursal.listar(activity).find { it.id == idSucursal }
        relacionActual = modeloRelacion.listar(activity).find { it.id == idRelacion }
        variables = modeloVariables.listar(activity)

        if (!datosValidos(sucursal, relacionActual, variables)) {
            vista.mostrarError("Error cargando datos.")
            return
        }

        vista.setUbicacionSucursal(LatLng(sucursal!!.latitud, sucursal.longitud))

        vista.onBtnMarcarClick { onBtnMarcarClick() }
        vista.onBtnLimpiarClick { onBtnLimpiarClick() }
        vista.onBtnCalcularClick { onBtnCalcularClick() }
        vista.onBtnAtrasClick { volverADisponibilidad() }
    }

    // ===== Lógica de cada botón =====

    private fun onBtnMarcarClick(): Unit {
        vista.marcarPuntoEnCentro()
    }

    private fun onBtnLimpiarClick(): Unit {
        vista.limpiarRuta()
    }

    private fun onBtnCalcularClick(): Unit {
        val puntos = vista.getPuntosRuta()
        if (puntos.size < 2) {
            vista.mostrarError("Trazá al menos 2 puntos.")
            return
        }

        val distancia = SphericalUtil.computeLength(puntos)
        val largoAuto = variables[2].valor
        val litrosPorAuto = variables[1].valor
        val tiempoCarga = variables[0].valor

        val autosEnFila = distancia / largoAuto
        val litrosNecesarios = autosEnFila * litrosPorAuto
        val alcanza = relacionActual!!.combustibleDisponible >= litrosNecesarios
        val tiempoTotal = (autosEnFila * tiempoCarga) / relacionActual!!.cantidadBombas

        vista.mostrarResultado(tiempoTotal, alcanza)
    }

    private fun volverADisponibilidad(): Unit {
        val intent = Intent(activity, disponibilidadActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    private fun datosValidos(suc: mSucursal?, rel: mSucursalCombustible?, vars: List<mVariable>): Boolean {
        return suc != null && rel != null && vars.size >= 3
    }
}
