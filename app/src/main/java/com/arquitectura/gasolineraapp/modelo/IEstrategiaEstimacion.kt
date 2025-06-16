package com.arquitectura.gasolineraapp.modelo

import com.google.android.gms.maps.model.LatLng

interface IEstrategiaEstimacion {
    fun calcularEstimacion(
        puntosRuta: List<LatLng>,
        relacion: mSucursalCombustible,
        variables: List<mVariable>
    ): EstimacionResultado
}

data class EstimacionResultado(
    val tiempo: Double,
    val alcanza: Boolean
)
