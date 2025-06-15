package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.modelo.mVariable
import com.arquitectura.gasolineraapp.vista.variables.vVariable
import com.arquitectura.gasolineraapp.vista.disponibilidad.disponibilidadActivity
import com.arquitectura.gasolineraapp.vista.sucursal.sucursalActivity
import com.arquitectura.gasolineraapp.vista.sucursalcombustible.sucursalCombustibleActivity
import com.arquitectura.gasolineraapp.vista.combustible.combustibleActivity

class cVariable(private val activity: Activity) {

    private val vista = vVariable(activity)
    private val modelo = mVariable()
    private var lista = listOf<mVariable>()
    private var seleccion: mVariable? = null

    fun iniciar(): Unit {
        vista.onBtnGuardarClick { onGuardarClick() }
        vista.onBtnMenuClick { vista.abrirDrawer() }
        vista.onItemMenuClick { itemId -> onMenuSeleccionado(itemId) }
        vista.onItemListaClick { index -> onItemSeleccionado(index) }
        mostrarLista()
    }

    private fun onGuardarClick(): Unit {
        if (!vista.hayVariableSeleccionada()) {
            vista.mostrarMensaje("Seleccione una constante para editar")
            return
        }

        val variable = seleccion ?: return
        editarVariable(variable)
    }


    private fun onItemSeleccionado(index: Int): Unit {
        seleccion = lista[index]
        AlertDialog.Builder(activity)
            .setTitle("Editar variable")
            .setMessage("¿Deseas editar el valor de esta variable?")
            .setPositiveButton("Sí") { _, _ ->
                vista.setDatos(
                    id = seleccion!!.id,
                    nombre = seleccion!!.nombre,
                    unidad = seleccion!!.unidad,
                    valor = seleccion!!.valor
                )
            }
            .setNegativeButton("No") { _, _ ->
                seleccion = null
                vista.limpiarCampos()
            }
            .show()
    }

    private fun onMenuSeleccionado(itemId: Int): Unit {
        when (itemId) {
            R.id.nav_inicio -> ir(disponibilidadActivity::class.java)
            R.id.nav_sucursal -> ir(sucursalActivity::class.java)
            R.id.nav_combustible -> ir(combustibleActivity::class.java)
            R.id.nav_sucursal_combustible -> ir(sucursalCombustibleActivity::class.java)
            R.id.nav_constantes -> vista.mostrarMensaje("Ya estás en Variables")
        }
        vista.cerrarDrawer()
    }

    // === CRUD explícito ===

    private fun editarVariable(variable: mVariable): Unit {
        val nuevoValor = vista.getNuevoValor()

        if (nuevoValor == null) {
            vista.mostrarMensaje("Ingrese un valor válido")
            return
        }

        variable.valor = nuevoValor
        variable.actualizar(activity)
        vista.mostrarMensaje("Constante actualizada")
        vista.limpiarCampos()
        seleccion = null
        mostrarLista()
    }


    private fun mostrarLista(): Unit {
        lista = modelo.listar(activity)
        val listaTexto = lista.map {
            "ID: ${it.id}\n${it.nombre}\n${it.unidad}\nValor: ${it.valor}"
        }
        val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, listaTexto)
        vista.mostrarLista(adapter)
    }

    private fun ir(destino: Class<*>): Unit {
        val intent = Intent(activity, destino)
        activity.startActivity(intent)
        activity.finish()
    }
}
