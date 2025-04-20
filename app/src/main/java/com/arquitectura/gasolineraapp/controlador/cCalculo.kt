package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.arquitectura.gasolineraapp.modelo.*
import com.arquitectura.gasolineraapp.vista.calculo.vCalculoActivity

class cCalculo(private val activity: Activity, private val idSucursal: Int, private val idSucursalCombustible: Int) {

    private val vista = vCalculoActivity(activity)
    private val sucursalModel = mSucursal()
    private val sucursalCombustibleModel = mSucursalCombustible()
    private val constantesModel = mConstante()

    private var metrosDibujados: Double = 0.0

    init {
        cargarDatosMapa()
        configurarEventos()
    }

    private fun cargarDatosMapa() {
        val sucursal = sucursalModel.obtenerPorId(activity, idSucursal)
        sucursal?.let {
            vista.mostrarMapaEnUbicacion(it.latitud, it.longitud)
        }
    }

    private fun configurarEventos() {
        vista.setOnDibujarClick {
            vista.abrirMapaParaDibujar { distancia ->
                metrosDibujados = distancia
                Toast.makeText(activity, "Distancia dibujada: ${distancia}m", Toast.LENGTH_SHORT).show()
            }
        }

        vista.setOnCalcularClick {
            val litrosDisponibles = sucursalCombustibleModel.obtenerLitrosDisponibles(activity, idSucursalCombustible)
            val bombas = sucursalCombustibleModel.obtenerBombas(activity, idSucursalCombustible)

            val tiempoCarga = constantesModel.obtenerValorPorNombre(activity, "tiempo_carga_auto")
            val litrosPorAuto = constantesModel.obtenerValorPorNombre(activity, "litros_promedio_auto")
            val largoAuto = constantesModel.obtenerValorPorNombre(activity, "longitud_auto")

            val autosEstimados = (metrosDibujados / largoAuto).toInt()
            val consumoEstimado = autosEstimados * litrosPorAuto
            val tiempoEstimado = (autosEstimados.toDouble() / bombas) * tiempoCarga

            val alcanza = litrosDisponibles >= consumoEstimado

            vista.mostrarResultado(tiempoEstimado, alcanza)
        }
    }
}
