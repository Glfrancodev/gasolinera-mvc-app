package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import android.widget.ArrayAdapter
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.modelo.*
import com.arquitectura.gasolineraapp.vista.disponibilidad.vDisponibilidadActivity
import com.arquitectura.gasolineraapp.vista.sucursal.vSucursalActivity
import com.arquitectura.gasolineraapp.vista.constante.vConstanteActivity

class cDisponibilidad(private val activity: Activity) {
    private val vista = vDisponibilidadActivity(activity)
    private val modeloCombustible = mTipoCombustible()
    private val modeloSucursal = mSucursal()
    private val modeloRelacion = mSucursalCombustible()

    private var combustibles = listOf<mTipoCombustible>()
    private var relaciones = listOf<mSucursalCombustible>()

    fun iniciar() {
        combustibles = modeloCombustible.listar(activity)
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, combustibles.map { it.nombre })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vista.setSpinner(adapter)

        vista.onBtnCargarClick {
            val index = vista.getCombustibleSeleccionado()
            if (index < 0) return@onBtnCargarClick

            val idCombustible = combustibles[index].id
            relaciones = modeloRelacion.listar(activity).filter { it.idCombustible == idCombustible }

            val sucursales = modeloSucursal.listar(activity)
            val listaTexto = relaciones.mapNotNull { rel ->
                val suc = sucursales.find { it.id == rel.idSucursal }
                suc?.let {
                    "ID: ${it.id}\nNombre: ${it.nombre}\nDirección: ${it.direccion}\nLitros: ${rel.combustibleDisponible}\nHora de Medición: ${rel.horaMedicion}"
                }
            }
            vista.mostrarLista(ArrayAdapter(activity, android.R.layout.simple_list_item_1, listaTexto))
        }

        vista.onBtnMenuClick { vista.abrirDrawer() }

        vista.onItemMenuClick { itemId ->
            when (itemId) {
                R.id.nav_sucursal -> ir(vSucursalActivity::class.java)
                R.id.nav_constantes -> ir(vConstanteActivity::class.java)
            }
            vista.cerrarDrawer()
        }
    }

    private fun ir(destino: Class<*>) {
        activity.startActivity(Intent(activity, destino))
        activity.finish()
    }
}
