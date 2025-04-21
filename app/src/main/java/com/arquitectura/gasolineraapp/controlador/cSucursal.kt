package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.modelo.mSucursal
import com.arquitectura.gasolineraapp.vista.variables.variableActivity
import com.arquitectura.gasolineraapp.vista.disponibilidad.disponibilidadActivity
import com.arquitectura.gasolineraapp.vista.sucursal.*
import com.arquitectura.gasolineraapp.vista.combustible.combustibleActivity
import com.arquitectura.gasolineraapp.vista.sucursalcombustible.sucursalCombustibleActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class cSucursal(private val activity: Activity) {

    private var vista: vSucursal? = null
    private var vistaMapa: vMapa? = null
    private val modelo = mSucursal()

    private var modoActualizar = false
    private var sucursalSeleccionada: mSucursal? = null

    init {
        if (activity is MapaActivity) {
            vistaMapa = vMapa(activity)
            iniciarDesdeMapa()
        } else {
            vista = vSucursal(activity)
            iniciarDesdeFormulario()
        }
    }

    fun iniciar() {
        if (activity !is MapaActivity) {
            iniciarDesdeFormulario()
        }
    }

    private val launcherMapa = (activity as androidx.activity.ComponentActivity)
        .registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val lat = data?.getDoubleExtra("latitud", 0.0) ?: 0.0
                val lon = data?.getDoubleExtra("longitud", 0.0) ?: 0.0
                val nombre = data?.getStringExtra("nombre") ?: ""
                val direccion = data?.getStringExtra("direccion") ?: ""

                val nuevaSucursal = mSucursal(0,nombre, direccion, lat, lon)
                val exito = nuevaSucursal.insertar(activity)

                if (exito) {
                    vista?.mostrarMensaje("Sucursal agregada con ubicación")
                    vista?.limpiarCampos()
                    mostrarLista()
                } else {
                    vista?.mostrarMensaje("Error al guardar sucursal")
                }
            }
        }

    private fun iniciarDesdeFormulario() {
        vista?.onBtnGuardarClick {
            val nombre = vista?.getNombre() ?: ""
            val direccion = vista?.getDireccion() ?: ""

            if (nombre.isBlank() || direccion.isBlank()) {
                vista?.mostrarMensaje("Completa todos los campos")
                return@onBtnGuardarClick
            }

            if (modoActualizar && sucursalSeleccionada != null) {
                sucursalSeleccionada!!.nombre = nombre
                sucursalSeleccionada!!.direccion = direccion
                val exito = sucursalSeleccionada!!.actualizar(activity)
                if (exito) vista?.mostrarMensaje("Sucursal actualizada")
                vista?.limpiarCampos()
                modoActualizar = false
                sucursalSeleccionada = null
                mostrarLista()
            } else {
                val intent = Intent(activity, MapaActivity::class.java)
                intent.putExtra("nombre", nombre)
                intent.putExtra("direccion", direccion)
                launcherMapa.launch(intent)
            }
        }

        vista?.onItemSeleccionado { posicion ->
            val lista = modelo.listar(activity)
            val sucursal = lista[posicion]
            val opciones = arrayOf("✏️ Editar", "🗑️ Eliminar")

            AlertDialog.Builder(activity)
                .setTitle("Acciones para ID: ${sucursal.id}")
                .setItems(opciones) { _, opcion ->
                    when (opcion) {
                        0 -> {
                            vista?.setNombre(sucursal.nombre)
                            vista?.setDireccion(sucursal.direccion)
                            vista?.setModoActualizar()
                            modoActualizar = true
                            sucursalSeleccionada = sucursal
                        }
                        1 -> {
                            sucursal.eliminar(activity)
                            vista?.mostrarMensaje("Sucursal eliminada")
                            mostrarLista()
                        }
                    }
                }.show()
        }

        vista?.onBtnMenuClick {
            vista?.abrirDrawer()
        }

        vista?.onItemMenuClick { itemId ->
            when (itemId) {
                R.id.nav_inicio -> ir(disponibilidadActivity::class.java)
                R.id.nav_sucursal -> vista?.mostrarMensaje("Ya estás en Sucursal")
                R.id.nav_combustible -> ir(combustibleActivity::class.java)
                R.id.nav_sucursal_combustible -> ir(sucursalCombustibleActivity::class.java)
                R.id.nav_constantes -> ir(variableActivity::class.java)
            }
            vista?.cerrarDrawer()
        }

        mostrarLista()
    }

    private fun iniciarDesdeMapa() {
        val extras = (activity as MapaActivity).intent.extras
        val nombre = extras?.getString("nombre") ?: ""
        val direccion = extras?.getString("direccion") ?: ""

        vistaMapa?.mapView?.getMapAsync { googleMap ->
            val santaCruz = LatLng(-17.7833276, -63.1821408)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(santaCruz, 14f))

            googleMap.setOnMapLongClickListener { latLng ->
                vistaMapa?.mapView?.tag = latLng
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(latLng).title("Ubicación seleccionada"))
            }
        }

        vistaMapa?.btnConfirmar?.setOnClickListener {
            val latLng = vistaMapa?.mapView?.tag as? LatLng
            val resultIntent = Intent().apply {
                putExtra("latitud", latLng?.latitude ?: 0.0)
                putExtra("longitud", latLng?.longitude ?: 0.0)
                putExtra("nombre", nombre)
                putExtra("direccion", direccion)
            }
            activity.setResult(Activity.RESULT_OK, resultIntent)
            activity.finish()
        }
    }

    private fun mostrarLista() {
        val lista = modelo.listar(activity).map {
            "ID: ${it.id}\n${it.nombre}\n${it.direccion}"
        }
        vista?.mostrarLista(lista)
    }

    private fun ir(destino: Class<*>) {
        val intent = Intent(activity, destino)
        activity.startActivity(intent)
        activity.finish()
    }
}
