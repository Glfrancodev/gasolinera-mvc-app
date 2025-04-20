// Ruta: com.arquitectura.gasolineraapp.controlador.cConstante.kt

package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.modelo.mConstante
import com.arquitectura.gasolineraapp.vista.constante.vConstanteActivity
import com.arquitectura.gasolineraapp.vista.disponibilidad.disponibilidadActivity
import com.arquitectura.gasolineraapp.vista.sucursal.sucursalActivity
import com.arquitectura.gasolineraapp.vista.sucursalcombustible.sucursalCombustibleActivity
import com.arquitectura.gasolineraapp.vista.combustible.combustibleActivity

class cConstante(private val activity: Activity) {

    private val vista = vConstanteActivity(activity)
    private val modelo = mConstante()
    private var lista = listOf<mConstante>()
    private var seleccion: mConstante? = null

    fun iniciar() {
        // Botón Guardar
        vista.onBtnGuardarClick {
            val constante = seleccion
            if (constante != null) {
                val nuevoValor = vista.etValor.text.toString().toDoubleOrNull()
                if (nuevoValor != null) {
                    constante.valor = nuevoValor
                    constante.actualizar(activity)
                    vista.mostrarMensaje("Constante actualizada")
                    vista.limpiarCampos()
                    seleccion = null
                    mostrarLista()
                } else {
                    vista.mostrarMensaje("Ingrese un valor válido")
                }
            } else {
                vista.mostrarMensaje("Seleccione una constante para editar")
            }
        }

        // Sidebar: botón menú
        vista.onBtnMenuClick {
            vista.abrirDrawer()
        }

        // Sidebar: ítems
        vista.onItemMenuClick { itemId ->
            when (itemId) {
                R.id.nav_inicio -> ir(disponibilidadActivity::class.java)
                R.id.nav_sucursal -> ir(sucursalActivity::class.java)
                R.id.nav_combustible -> ir(combustibleActivity::class.java)
                R.id.nav_sucursal_combustible -> ir(sucursalCombustibleActivity::class.java)
                R.id.nav_constantes -> vista.mostrarMensaje("Ya estás en Constantes")
            }
            vista.cerrarDrawer()
        }

        // Selección de constante
        vista.onItemListaClick { index ->
            seleccion = lista[index]
            AlertDialog.Builder(activity)
                .setTitle("Editar constante")
                .setMessage("¿Deseas editar el valor de esta constante?")
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

        // Mostrar lista inicial
        mostrarLista()
    }

    private fun mostrarLista() {
        lista = modelo.listar(activity)
        val listaTexto = lista.map {
            "ID: ${it.id}\n${it.nombre}\n${it.unidad}\nValor: ${it.valor}"
        }
        val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, listaTexto)
        vista.mostrarLista(adapter)
    }

    private fun ir(destino: Class<*>) {
        val intent = Intent(activity, destino)
        activity.startActivity(intent)
        activity.finish()
    }
}
