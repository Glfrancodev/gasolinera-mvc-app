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
        vista.onBtnGuardarClick { onGuardarClick() }
        vista.onItemSeleccionado { posicion -> onItemSeleccionado(posicion) }
        vista.onBtnMenuClick { vista.abrirDrawer() }
        vista.onItemMenuClick { itemId -> onMenuSeleccionado(itemId) }
        mostrarLista()
    }

    private fun onGuardarClick() {
        val nombre = vista.getNombre()
        val descripcion = vista.getDescripcion()

        if (nombre.isBlank() || descripcion.isBlank()) {
            vista.mostrarMensaje("Completa todos los campos")
            return
        }

        if (modoActualizar && seleccionado != null) {
            editarTipoCombustible(nombre, descripcion)
        } else {
            agregarTipoCombustible(nombre, descripcion)
        }

        vista.limpiarCampos()
        mostrarLista()
    }

    private fun onItemSeleccionado(posicion: Int) {
        val lista = modelo.listar(activity)
        val tipo = lista[posicion]
        val opciones = arrayOf("✏️ Editar", "🗑️ Eliminar")

        AlertDialog.Builder(activity)
            .setTitle("Acciones para ID: ${tipo.id}")
            .setItems(opciones) { _, opcion ->
                when (opcion) {
                    0 -> prepararEdicion(tipo)
                    1 -> eliminarTipoCombustible(tipo)
                }
            }.show()
    }

    private fun onMenuSeleccionado(itemId: Int) {
        when (itemId) {
            R.id.nav_inicio -> ir(disponibilidadActivity::class.java)
            R.id.nav_sucursal -> ir(sucursalActivity::class.java)
            R.id.nav_combustible -> vista.mostrarMensaje("Ya estás en Tipo Combustible")
            R.id.nav_sucursal_combustible -> ir(sucursalCombustibleActivity::class.java)
            R.id.nav_constantes -> ir(variableActivity::class.java)
        }
        vista.cerrarDrawer()
    }

    // --- Métodos CRUD explícitos ---
    private fun agregarTipoCombustible(nombre: String, descripcion: String): Unit {
        val nuevo = mTipoCombustible(nombre = nombre, descripcion = descripcion)
        nuevo.insertar(activity)
        vista.mostrarMensaje("Tipo de combustible guardado")
    }

    private fun editarTipoCombustible(nombre: String, descripcion: String): Unit {
        seleccionado!!.nombre = nombre
        seleccionado!!.descripcion = descripcion
        seleccionado!!.actualizar(activity)
        vista.mostrarMensaje("Tipo de combustible actualizado")
        modoActualizar = false
        seleccionado = null
    }

    private fun eliminarTipoCombustible(tipo: mTipoCombustible): Unit {
        tipo.eliminar(activity)
        vista.mostrarMensaje("Eliminado correctamente")
        mostrarLista()
    }

    private fun prepararEdicion(tipo: mTipoCombustible): Unit {
        vista.setNombre(tipo.nombre)
        vista.setDescripcion(tipo.descripcion)
        vista.setModoActualizar()
        modoActualizar = true
        seleccionado = tipo
    }

    private fun mostrarLista(): Unit {
        val lista = modelo.listar(activity).map {
            "ID: ${it.id}\n${it.nombre}\n${it.descripcion}"
        }
        vista.mostrarLista(lista)
    }

    private fun ir(clase: Class<*>): Unit {
        val intent = Intent(activity, clase)
        activity.startActivity(intent)
        activity.finish()
    }
}
