package com.arquitectura.gasolineraapp.modelo.strategy

import com.arquitectura.gasolineraapp.modelo.mSucursalCombustible
import com.arquitectura.gasolineraapp.modelo.mVariable
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
