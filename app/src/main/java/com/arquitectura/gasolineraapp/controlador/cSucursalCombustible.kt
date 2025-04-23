package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.modelo.*
import com.arquitectura.gasolineraapp.vista.variables.variableActivity
import com.arquitectura.gasolineraapp.vista.disponibilidad.disponibilidadActivity
import com.arquitectura.gasolineraapp.vista.sucursal.sucursalActivity
import com.arquitectura.gasolineraapp.vista.sucursalcombustible.vSucursalCombustible
import com.arquitectura.gasolineraapp.vista.combustible.combustibleActivity

import java.text.SimpleDateFormat
import java.util.*

class cSucursalCombustible(private val activity: Activity) {

    private val vista = vSucursalCombustible(activity)
    private val modelo = mSucursalCombustible()
    private var lista = listOf<mSucursalCombustible>()
    private var modoActualizar = false
    private var seleccion: mSucursalCombustible? = null

    private val modeloSucursal = mSucursal()
    private val modeloCombustible = mTipoCombustible()
    private var sucursales = listOf<mSucursal>()
    private var combustibles = listOf<mTipoCombustible>()

    fun iniciar(): Unit {
        cargarSpinners()
        vista.onBtnGuardarClick { onGuardarClick() }
        vista.onItemSeleccionado { index -> onItemSeleccionado(index) }
        vista.onBtnMenuClick { vista.abrirDrawer() }
        vista.onItemMenuClick { itemId -> onMenuSeleccionado(itemId) }
        mostrarLista()
    }

    private fun onGuardarClick(): Unit {
        if (modoActualizar && seleccion != null) {
            editarSucursalCombustible()
        } else {
            agregarSucursalCombustible()
        }

        vista.limpiarCampos()
        modoActualizar = false
        seleccion = null
        mostrarLista()
    }


    private fun onItemSeleccionado(index: Int): Unit {
        val item = lista[index]
        val posSucursal = sucursales.indexOfFirst { it.id == item.idSucursal }
        val posCombustible = combustibles.indexOfFirst { it.id == item.idCombustible }

        AlertDialog.Builder(activity)
            .setTitle("Acciones para ID: ${item.id}")
            .setItems(arrayOf("✏️ Editar", "🗑️ Eliminar")) { _, opcion ->
                when (opcion) {
                    0 -> prepararEdicion(item, posSucursal, posCombustible)
                    1 -> eliminarSucursalCombustible(item)
                }
            }.show()
    }

    private fun onMenuSeleccionado(itemId: Int): Unit {
        when (itemId) {
            R.id.nav_inicio -> ir(disponibilidadActivity::class.java)
            R.id.nav_sucursal -> ir(sucursalActivity::class.java)
            R.id.nav_combustible -> ir(combustibleActivity::class.java)
            R.id.nav_sucursal_combustible -> vista.mostrarMensaje("Ya estás en Sucursal-Combustible")
            R.id.nav_constantes -> ir(variableActivity::class.java)
        }
        vista.cerrarDrawer()
    }

    // === CRUD explícitos ===

    private fun agregarSucursalCombustible(): Unit {
        val cantidad = vista.getCantidadBombas()
        val litros = vista.getLitrosDisponibles()
        val posSucursal = vista.getSucursalSeleccionada()
        val posCombustible = vista.getCombustibleSeleccionado()

        if (cantidad == null || litros == null || posSucursal < 0 || posCombustible < 0) {
            vista.mostrarMensaje("Completa todos los campos")
            return
        }

        val idSucursal = sucursales[posSucursal].id
        val idCombustible = combustibles[posCombustible].id

        val nuevo = mSucursalCombustible(
            idSucursal = idSucursal,
            idCombustible = idCombustible,
            cantidadBombas = cantidad,
            horaMedicion = obtenerFechaActual(),
            combustibleDisponible = litros
        )

        if (nuevo.insertar(activity)) {
            vista.mostrarMensaje("Creado correctamente")
        } else {
            vista.mostrarMensaje("Error al crear")
        }
    }


    private fun editarSucursalCombustible(): Unit {
        val cantidad = vista.getCantidadBombas()
        val litros = vista.getLitrosDisponibles()
        val posSucursal = vista.getSucursalSeleccionada()
        val posCombustible = vista.getCombustibleSeleccionado()

        if (cantidad == null || litros == null || posSucursal < 0 || posCombustible < 0) {
            vista.mostrarMensaje("Completa todos los campos")
            return
        }

        val idSucursal = sucursales[posSucursal].id
        val idCombustible = combustibles[posCombustible].id

        seleccion!!.idSucursal = idSucursal
        seleccion!!.idCombustible = idCombustible
        seleccion!!.cantidadBombas = cantidad
        seleccion!!.combustibleDisponible = litros

        if (seleccion!!.actualizar(activity)) {
            vista.mostrarMensaje("Actualizado correctamente")
        } else {
            vista.mostrarMensaje("Error al actualizar")
        }
    }


    private fun eliminarSucursalCombustible(item: mSucursalCombustible): Unit {
        item.eliminar(activity)
        vista.mostrarMensaje("Eliminado")
        mostrarLista()
        vista.limpiarCampos()
        modoActualizar = false
        seleccion = null
    }

    private fun prepararEdicion(item: mSucursalCombustible, posSucursal: Int, posCombustible: Int): Unit {
        seleccion = item
        vista.spinnerSucursal.setSelection(posSucursal)
        vista.spinnerCombustible.setSelection(posCombustible)
        vista.etCantidad.setText(item.cantidadBombas.toString())
        vista.etCombustible.setText(item.combustibleDisponible.toString())
        vista.setModoActualizar()
        modoActualizar = true
    }

    private fun mostrarLista(): Unit {
        lista = modelo.listar(activity)
        val mostrar = lista.map { registro ->
            val nombreSuc = sucursales.find { it.id == registro.idSucursal }?.nombre ?: "¿?"
            val nombreComb = combustibles.find { it.id == registro.idCombustible }?.nombre ?: "¿?"
            "ID: ${registro.id}\nSucursal: $nombreSuc\nCombustible: $nombreComb\nBombas: ${registro.cantidadBombas}\nLitros: ${registro.combustibleDisponible}"
        }
        vista.mostrarLista(mostrar)
    }

    private fun     cargarSpinners(): Unit {
        sucursales = modeloSucursal.listar(activity)
        combustibles = modeloCombustible.listar(activity)

        val adapterSuc = ArrayAdapter(activity, android.R.layout.simple_spinner_item, sucursales.map { it.nombre })
        adapterSuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vista.setSpinnerSucursal(adapterSuc)

        val adapterComb = ArrayAdapter(activity, android.R.layout.simple_spinner_item, combustibles.map { it.nombre })
        adapterComb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vista.setSpinnerCombustible(adapterComb)
    }

    private fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun ir(clase: Class<*>): Unit {
        val intent = Intent(activity, clase)
        activity.startActivity(intent)
        activity.finish()
    }
}
