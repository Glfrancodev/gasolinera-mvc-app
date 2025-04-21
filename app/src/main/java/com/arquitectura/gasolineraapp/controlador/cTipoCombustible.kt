package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.modelo.mTipoCombustible
import com.arquitectura.gasolineraapp.vista.combustible.vTipoCombustible
import com.arquitectura.gasolineraapp.vista.variables.variableActivity
import com.arquitectura.gasolineraapp.vista.disponibilidad.disponibilidadActivity
import com.arquitectura.gasolineraapp.vista.sucursal.sucursalActivity
import com.arquitectura.gasolineraapp.vista.sucursalcombustible.sucursalCombustibleActivity

class cTipoCombustible(private val activity: Activity) {

    private val vista = vTipoCombustible(activity)
    private val modelo = mTipoCombustible()

    private var modoActualizar = false
    private var seleccionado: mTipoCombustible? = null

    fun iniciar() {
        vista.onBtnGuardarClick {
            val nombre = vista.getNombre()
            val descripcion = vista.getDescripcion()

            if (nombre.isBlank() || descripcion.isBlank()) {
                vista.mostrarMensaje("Completa todos los campos")
                return@onBtnGuardarClick
            }

            if (modoActualizar && seleccionado != null) {
                seleccionado!!.nombre = nombre
                seleccionado!!.descripcion = descripcion
                seleccionado!!.actualizar(activity)
                vista.mostrarMensaje("Tipo de combustible actualizado")
                modoActualizar = false
                seleccionado = null
            } else {
                val nuevo = mTipoCombustible(nombre = nombre, descripcion = descripcion)
                nuevo.insertar(activity)
                vista.mostrarMensaje("Tipo de combustible guardado")
            }

            vista.limpiarCampos()
            mostrarLista()
        }

        vista.onItemSeleccionado { posicion ->
            val lista = modelo.listar(activity)
            val tipo = lista[posicion]
            val opciones = arrayOf("✏️ Editar", "🗑️ Eliminar")

            AlertDialog.Builder(activity)
                .setTitle("Acciones para ID: ${tipo.id}")
                .setItems(opciones) { _, opcion ->
                    when (opcion) {
                        0 -> {
                            vista.setNombre(tipo.nombre)
                            vista.setDescripcion(tipo.descripcion)
                            vista.setModoActualizar()
                            modoActualizar = true
                            seleccionado = tipo
                        }
                        1 -> {
                            tipo.eliminar(activity)
                            vista.mostrarMensaje("Eliminado correctamente")
                            mostrarLista()
                        }
                    }
                }.show()
        }

        vista.onBtnMenuClick { vista.abrirDrawer() }

        vista.onItemMenuClick { itemId ->
            when (itemId) {
                R.id.nav_inicio -> ir(disponibilidadActivity::class.java)
                R.id.nav_sucursal -> ir(sucursalActivity::class.java)
                R.id.nav_combustible -> vista.mostrarMensaje("Ya estás en Tipo Combustible")
                R.id.nav_sucursal_combustible -> ir(sucursalCombustibleActivity::class.java)
                R.id.nav_constantes -> ir(variableActivity::class.java)
            }
            vista.cerrarDrawer()
        }

        mostrarLista()
    }

    private fun mostrarLista() {
        val lista = modelo.listar(activity).map {
            "ID: ${it.id}\n${it.nombre}\n${it.descripcion}"
        }
        vista.mostrarLista(lista)
    }

    private fun ir(clase: Class<*>) {
        val intent = Intent(activity, clase)
        activity.startActivity(intent)
        activity.finish()
    }
}
