package com.arquitectura.gasolineraapp.modelo.strategy

import com.arquitectura.gasolineraapp.modelo.mSucursalCombustible
import com.arquitectura.gasolineraapp.modelo.mVariable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

class EstimacionEstandar : IEstrategiaEstimacion {
    override fun calcularEstimacion(
        puntosRuta: List<LatLng>,
        relacion: mSucursalCombustible,
        variables: List<mVariable> // No se usará en Estándar, pero se respeta la firma
    ): EstimacionResultado {
        val distancia = SphericalUtil.computeLength(puntosRuta)

        val largoAuto = 4.5  // metros
        val litrosPorAuto = 40.0  // litros
        val tiempoCarga = 3.0  // minutos

        val autosEnFila = distancia / largoAuto
        val litrosNecesarios = autosEnFila * litrosPorAuto
        val alcanza = relacion.combustibleDisponible >= litrosNecesarios
        val tiempoTotal = (autosEnFila * tiempoCarga) / relacion.cantidadBombas

        return EstimacionResultado(tiempo = tiempoTotal, alcanza = alcanza)
    }
}
