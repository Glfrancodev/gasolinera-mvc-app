package com.arquitectura.gasolineraapp.controlador

import android.app.Activity
import android.content.Intent
import android.widget.ArrayAdapter
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.modelo.*
import com.arquitectura.gasolineraapp.vista.calculo.calculoActivity
import com.arquitectura.gasolineraapp.vista.constante.constanteActivity
import com.arquitectura.gasolineraapp.vista.disponibilidad.vDisponibilidadActivity
import com.arquitectura.gasolineraapp.vista.sucursal.sucursalActivity
import com.arquitectura.gasolineraapp.vista.sucursalcombustible.sucursalCombustibleActivity
import com.arquitectura.gasolineraapp.vista.combustible.combustibleActivity

class cDisponibilidad(private val activity: Activity) {

    private val vista = vDisponibilidadActivity(activity)
    private val modeloCombustible = mTipoCombustible()
    private val modeloSucursal = mSucursal()
    private val modeloRelacion = mSucursalCombustible()

    private var combustibles = listOf<mTipoCombustible>()
    private var relaciones = listOf<mSucursalCombustible>()

    fun iniciar() {
        // Cargar spinner de combustibles
        combustibles = modeloCombustible.listar(activity)
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, combustibles.map { it.nombre })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vista.setSpinner(adapter)

        // Acción del botón cargar
        vista.onBtnCargarClick {
            val index = vista.getCombustibleSeleccionado()
            if (index < 0) {
                vista.mostrarMensaje("Seleccione un tipo de combustible")
                return@onBtnCargarClick
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

        // Evento al hacer clic sobre una sucursal (nueva funcionalidad)
        vista.setOnItemSelected { posicion ->
            val relSeleccionada = relaciones.getOrNull(posicion)
            if (relSeleccionada != null) {
                val intent = Intent(activity, calculoActivity::class.java)
                intent.putExtra("idSucursal", relSeleccionada.idSucursal)
                intent.putExtra("idSucursalCombustible", relSeleccionada.id)
                activity.startActivity(intent)
                activity.finish()
            }
        }

        // Botón menú lateral
        vista.onBtnMenuClick {
            vista.abrirDrawer()
        }

        // Opciones del sidebar
        vista.onItemMenuClick { itemId ->
            when (itemId) {
                R.id.nav_inicio -> vista.mostrarMensaje("Ya estás en Inicio")
                R.id.nav_sucursal -> ir(sucursalActivity::class.java)
                R.id.nav_combustible -> ir(combustibleActivity::class.java)
                R.id.nav_sucursal_combustible -> ir(sucursalCombustibleActivity::class.java)
                R.id.nav_constantes -> ir(constanteActivity::class.java)
            }
            vista.cerrarDrawer()
        }
    }

    private fun ir(destino: Class<*>) {
        val intent = Intent(activity, destino)
        activity.startActivity(intent)
        activity.finish()
    }
}
