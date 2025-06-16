package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import com.arquitectura.gasolineraapp.modelo.*
import com.arquitectura.gasolineraapp.modelo.strategy.CalculadoraContexto
import com.arquitectura.gasolineraapp.vista.calculo.vCalculo
import com.arquitectura.gasolineraapp.vista.disponibilidad.disponibilidadActivity
import com.google.android.gms.maps.model.LatLng

class cCalculo(private val activity: Activity) {
    private val contexto = CalculadoraContexto()  // Inicializas con alguna por defecto

    private val vista = vCalculo(activity)
    private val modeloSucursal = mSucursal()
    private val modeloRelacion = mSucursalCombustible()
    private val modeloVariables = mVariable()

    private var relacionActual: mSucursalCombustible? = null
    private var variables: List<mVariable> = emptyList()

    fun iniciar() {
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

    private fun onBtnMarcarClick() {
        vista.marcarPuntoEnCentro()
    }

    private fun onBtnLimpiarClick() {
        vista.limpiarRuta()
    }

    private fun onBtnCalcularClick() {
        val puntos = vista.getPuntosRuta()
        if (puntos.size < 2) {
            vista.mostrarError("Trazá al menos 2 puntos.")
            return
        }

        val modo = vista.getModoSeleccionado()
        contexto.setStrategyByModo(modo)

        val resultado = contexto.calcular(puntos, relacionActual!!, variables)

        if (resultado != null) {
            vista.mostrarResultado(resultado.tiempo, resultado.alcanza)
        } else {
            vista.mostrarError("Modo de cálculo no válido.")
        }
    }

    private fun volverADisponibilidad() {
        val intent = Intent(activity, disponibilidadActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    private fun datosValidos(suc: mSucursal?, rel: mSucursalCombustible?, vars: List<mVariable>): Boolean {
        return suc != null && rel != null && vars.size >= 3
    }
}
