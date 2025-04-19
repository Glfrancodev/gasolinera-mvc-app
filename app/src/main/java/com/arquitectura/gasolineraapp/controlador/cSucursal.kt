package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.modelo.mSucursal
import com.arquitectura.gasolineraapp.vista.vCalculoActivity
import com.arquitectura.gasolineraapp.vista.sucursal.vMapaActivity
import com.arquitectura.gasolineraapp.vista.sucursal.vSucursalActivity

class cSucursal(private val activity: Activity) {

    private val vista = vSucursalActivity(activity)
    private val modelo = mSucursal()

    private var modoActualizar = false
    private var sucursalSeleccionada: mSucursal? = null

    private var nombreTemporal: String = ""
    private var direccionTemporal: String = ""

    private val launcherMapa = (activity as androidx.activity.ComponentActivity)
        .registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val lat = data?.getDoubleExtra("latitud", 0.0) ?: 0.0
                val lon = data?.getDoubleExtra("longitud", 0.0) ?: 0.0

                val nuevaSucursal = mSucursal(
                    nombre = nombreTemporal,
                    direccion = direccionTemporal,
                    latitud = lat,
                    longitud = lon
                )

                val exito = nuevaSucursal.insertar(activity)
                if (exito) {
                    vista.mostrarMensaje("Sucursal agregada con ubicación")
                    vista.limpiarCampos()
                    mostrarLista()
                } else {
                    vista.mostrarMensaje("Error al guardar sucursal")
                }
            }
        }

    fun iniciar() {

        vista.onBtnGuardarClick {
            val nombre = vista.getNombre()
            val direccion = vista.getDireccion()

            if (nombre.isBlank() || direccion.isBlank()) {
                vista.mostrarMensaje("Completa todos los campos")
                return@onBtnGuardarClick
            }

            nombreTemporal = nombre
            direccionTemporal = direccion

            val intent = Intent(activity, vMapaActivity::class.java)
            launcherMapa.launch(intent)
        }

        vista.onItemSeleccionado { posicion ->
            val lista = modelo.listar(activity)
            val sucursal = lista[posicion]
            val opciones = arrayOf("✏️ Editar", "🗑️ Eliminar")

            AlertDialog.Builder(activity)
                .setTitle("Acciones para ID: ${sucursal.id}")
                .setItems(opciones) { _, opcion ->
                    when (opcion) {
                        0 -> {
                            vista.setNombre(sucursal.nombre)
                            vista.setDireccion(sucursal.direccion)
                            vista.setModoActualizar()
                            modoActualizar = true
                            sucursalSeleccionada = sucursal
                        }
                        1 -> {
                            sucursal.eliminar(activity)
                            vista.mostrarMensaje("Sucursal eliminada")
                            mostrarLista()
                        }
                    }
                }.show()
        }

        vista.onBtnMenuClick {
            vista.abrirDrawer()
        }

        vista.onItemMenuClick { itemId ->
            when (itemId) {
                R.id.nav_sucursal -> vista.mostrarMensaje("Ya estás en Sucursal")
                R.id.nav_calculo -> {
                    val intent = Intent(activity, vCalculoActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                }
            }
            vista.cerrarDrawer()
        }

        mostrarLista()
    }

    private fun mostrarLista() {
        val lista = modelo.listar(activity).map {
            "ID: ${it.id}\n${it.nombre}\n${it.direccion}"
        }
        vista.mostrarLista(lista)
    }
}
