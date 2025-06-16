package com.arquitectura.gasolineraapp.modelo

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

class EstimacionPersonalizada : IEstrategiaEstimacion {
    override fun calcularEstimacion(
        puntosRuta: List<LatLng>,
        relacion: mSucursalCombustible,
        variables: List<mVariable>
    ): EstimacionResultado {
        val distancia = SphericalUtil.computeLength(puntosRuta)

        val tiempoCarga = variables.find { it.nombre.equals("Tiempo promedio de carga por auto", ignoreCase = true) }?.valor ?: 3.0
        val litrosPorAuto = variables.find { it.nombre.equals("Cantidad promedio de litros por auto", ignoreCase = true) }?.valor ?: 40.0
        val largoAuto = variables.find { it.nombre.equals("Longitud promedio de un auto", ignoreCase = true) }?.valor ?: 4.5

        val autosEnFila = distancia / largoAuto
        val litrosNecesarios = autosEnFila * litrosPorAuto
        val alcanza = relacion.combustibleDisponible >= litrosNecesarios
        val tiempoTotal = (autosEnFila * tiempoCarga) / relacion.cantidadBombas

        return EstimacionResultado(tiempo = tiempoTotal, alcanza = alcanza)
    }
}
