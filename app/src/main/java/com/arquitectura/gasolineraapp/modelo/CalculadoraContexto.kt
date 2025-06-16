package com.arquitectura.gasolineraapp.modelo

import com.google.android.gms.maps.model.LatLng

class CalculadoraContexto {

    private var strategy: IEstrategiaEstimacion? = null

    fun setStrategyByModo(modo: String) {
        strategy = when (modo) {
            "Estándar" -> EstimacionEstandar()
            "Personalizado" -> EstimacionPersonalizada()
            else -> null
        }
    }

    fun calcular(
        puntosRuta: List<LatLng>,
        relacion: mSucursalCombustible,
        variables: List<mVariable>
    ): EstimacionResultado? {
        return strategy?.calcularEstimacion(puntosRuta, relacion, variables)
    }
}
