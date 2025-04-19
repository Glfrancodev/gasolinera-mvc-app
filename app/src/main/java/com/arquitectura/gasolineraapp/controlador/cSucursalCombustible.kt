package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.modelo.*
import com.arquitectura.gasolineraapp.vista.constante.vConstanteActivity
import com.arquitectura.gasolineraapp.vista.sucursal.vSucursalActivity
import com.arquitectura.gasolineraapp.vista.sucursalcombustible.vSucursalCombustibleActivity
import java.text.SimpleDateFormat
import java.util.*

class cSucursalCombustible(private val activity: Activity) {

    private val vista = vSucursalCombustibleActivity(activity)
    private val modelo = mSucursalCombustible()
    private var lista = listOf<mSucursalCombustible>()
    private var modoActualizar = false
    private var seleccion: mSucursalCombustible? = null

    private val modeloSucursal = mSucursal()
    private val modeloCombustible = mTipoCombustible()
    private var sucursales = listOf<mSucursal>()
    private var combustibles = listOf<mTipoCombustible>()

    fun iniciar() {
        cargarSpinners()

        vista.onBtnGuardarClick {
            val cantidad = vista.etCantidad.text.toString().toIntOrNull()
            val posSucursal = vista.spinnerSucursal.selectedItemPosition
            val posCombustible = vista.spinnerCombustible.selectedItemPosition

            if (cantidad == null || posSucursal < 0 || posCombustible < 0) {
                vista.mostrarMensaje("Completa todos los campos")
                return@onBtnGuardarClick
            }

            val idSucursal = sucursales[posSucursal].id
            val idCombustible = combustibles[posCombustible].id

            if (modoActualizar && seleccion != null) {
                seleccion!!.idSucursal = idSucursal
                seleccion!!.idCombustible = idCombustible
                seleccion!!.cantidadBombas = cantidad
                if (seleccion!!.actualizar(activity)) {
                    vista.mostrarMensaje("Actualizado correctamente")
                } else {
                    vista.mostrarMensaje("Error al actualizar")
                }
            } else {
                val fechaMedicion = obtenerFechaActual()
                val nuevo = mSucursalCombustible(
                    idSucursal = idSucursal,
                    idCombustible = idCombustible,
                    cantidadBombas = cantidad,
                )
                if (nuevo.insertar(activity)) {
                    vista.mostrarMensaje("Creado correctamente")
                } else {
                    vista.mostrarMensaje("Error al crear")
                }
            }

            vista.limpiarCampos()
            modoActualizar = false
            seleccion = null
            mostrarLista()
        }

        vista.onItemSeleccionado { index ->
            val item = lista[index]

            val posSucursal = sucursales.indexOfFirst { it.id == item.idSucursal }
            val posCombustible = combustibles.indexOfFirst { it.id == item.idCombustible }

            AlertDialog.Builder(activity)
                .setTitle("Acciones para ID: ${item.id}")
                .setItems(arrayOf("✏️ Editar", "🗑️ Eliminar")) { _, opcion ->
                    when (opcion) {
                        0 -> {
                            seleccion = item
                            vista.spinnerSucursal.setSelection(posSucursal)
                            vista.spinnerCombustible.setSelection(posCombustible)
                            vista.etCantidad.setText(item.cantidadBombas.toString())
                            vista.setModoActualizar()
                            modoActualizar = true
                        }
                        1 -> {
                            item.eliminar(activity)
                            vista.mostrarMensaje("Eliminado")
                            mostrarLista()
                            vista.limpiarCampos()
                            modoActualizar = false
                            seleccion = null
                        }
                    }
                }.show()
        }

        vista.onBtnMenuClick { vista.abrirDrawer() }

        vista.onItemMenuClick { itemId ->
            when (itemId) {
                R.id.nav_sucursal -> ir(vSucursalActivity::class.java)
                R.id.nav_constantes -> ir(vConstanteActivity::class.java)
            }
            vista.cerrarDrawer()
        }

        mostrarLista()
    }

    private fun mostrarLista() {
        lista = modelo.listar(activity)
        val mostrar = lista.map { registro ->
            val nombreSuc = sucursales.find { it.id == registro.idSucursal }?.nombre ?: "¿?"
            val nombreComb = combustibles.find { it.id == registro.idCombustible }?.nombre ?: "¿?"
            "ID: ${registro.id}\nSucursal: $nombreSuc\nCombustible: $nombreComb\nBombas: ${registro.cantidadBombas}"
        }
        vista.mostrarLista(mostrar)
    }

    private fun cargarSpinners() {
        sucursales = modeloSucursal.listar(activity)
        combustibles = modeloCombustible.listar(activity)

        val adapterSuc = ArrayAdapter(activity, android.R.layout.simple_spinner_item, sucursales.map { it.nombre })
        adapterSuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vista.setSpinnerSucursal(adapterSuc)

        val adapterComb = ArrayAdapter(activity, android.R.layout.simple_spinner_item, combustibles.map { it.nombre })
        adapterComb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vista.setSpinnerCombustible(adapterComb)
    }

    private fun ir(clase: Class<*>) {
        val intent = Intent(activity, clase)
        activity.startActivity(intent)
        activity.finish()
    }

    private fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
