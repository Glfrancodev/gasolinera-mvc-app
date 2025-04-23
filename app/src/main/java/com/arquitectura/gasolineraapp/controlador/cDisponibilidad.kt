package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import android.widget.ArrayAdapter
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.modelo.*
import com.arquitectura.gasolineraapp.vista.calculo.calculoActivity
import com.arquitectura.gasolineraapp.vista.variables.variableActivity
import com.arquitectura.gasolineraapp.vista.disponibilidad.vDisponibilidad
import com.arquitectura.gasolineraapp.vista.sucursal.sucursalActivity
import com.arquitectura.gasolineraapp.vista.sucursalcombustible.sucursalCombustibleActivity
import com.arquitectura.gasolineraapp.vista.combustible.combustibleActivity

class cDisponibilidad(private val activity: Activity) {

    private val vista = vDisponibilidad(activity)
    private val modeloCombustible = mTipoCombustible()
    private val modeloSucursal = mSucursal()
    private val modeloRelacion = mSucursalCombustible()

    private var combustibles = listOf<mTipoCombustible>()
    private var relaciones = listOf<mSucursalCombustible>()

    fun iniciar(): Unit {
        cargarCombustibles()
        vista.onBtnCargarClick { onBtnCargarClick() }
        vista.setOnItemSelected { posicion -> onItemSeleccionado(posicion) }
        vista.onBtnMenuClick { vista.abrirDrawer() }
        vista.onItemMenuClick { itemId -> onMenuSeleccionado(itemId) }
    }

    // === FUNCIONES SEPARADAS ===

    private fun cargarCombustibles(): Unit {
        combustibles = modeloCombustible.listar(activity)
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, combustibles.map { it.nombre })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vista.setSpinner(adapter)
    }

    private fun onBtnCargarClick(): Unit {
        val index = vista.getCombustibleSeleccionado()
        if (index < 0) {
            vista.mostrarMensaje("Seleccione un tipo de combustible")
            return
        }

        val idCombustible = combustibles[index].id
        relaciones = modeloRelacion.listar(activity).filter { it.idCombustible == idCombustible }

        val sucursales = modeloSucursal.listar(activity)
        val listaTexto = relaciones.mapNotNull { rel ->
            val suc = sucursales.find { it.id == rel.idSucursal }
            suc?.let {
                "ID: ${it.id}\nNombre: ${it.nombre}\nDirección: ${it.direccion}\nLitros: ${rel.combustibleDisponible}\nHora de Medición: ${rel.horaMedicion}"
            }
        }

        if (listaTexto.isEmpty()) {
            vista.mostrarMensaje("No hay sucursales registradas para este combustible.")
        }

        vista.mostrarLista(listaTexto)
    }

    private fun onItemSeleccionado(posicion: Int): Unit {
        val relSeleccionada = relaciones.getOrNull(posicion)
        if (relSeleccionada != null) {
            val intent = Intent(activity, calculoActivity::class.java)
            intent.putExtra("idSucursal", relSeleccionada.idSucursal)
            intent.putExtra("idSucursalCombustible", relSeleccionada.id)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    private fun onMenuSeleccionado(itemId: Int): Unit {
        when (itemId) {
            R.id.nav_inicio -> vista.mostrarMensaje("Ya estás en Inicio")
            R.id.nav_sucursal -> ir(sucursalActivity::class.java)
            R.id.nav_combustible -> ir(combustibleActivity::class.java)
            R.id.nav_sucursal_combustible -> ir(sucursalCombustibleActivity::class.java)
            R.id.nav_constantes -> ir(variableActivity::class.java)
        }
        vista.cerrarDrawer()
    }

    private fun ir(destino: Class<*>): Unit {
        val intent = Intent(activity, destino)
        activity.startActivity(intent)
        activity.finish()
    }
}
