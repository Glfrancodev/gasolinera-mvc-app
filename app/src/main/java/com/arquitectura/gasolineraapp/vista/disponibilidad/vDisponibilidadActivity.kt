package com.arquitectura.gasolineraapp.vista.disponibilidad

import android.app.Activity
import android.view.View
import android.widget.*
import androidx.core.view.GravityCompat
import com.arquitectura.gasolineraapp.R

class vDisponibilidadActivity(activity: Activity) {

    // Elementos de la vista
    private val spinnerCombustible: Spinner = activity.findViewById(R.id.spinnerCombustible)
    private val btnCargar: Button = activity.findViewById(R.id.btnCargar)
    private val listView: ListView = activity.findViewById(R.id.listDisponibilidad)

    private val btnMenu: ImageButton = activity.findViewById(R.id.btnMenu)
    private val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = activity.findViewById(R.id.drawerLayout)
    private val navigationView: com.google.android.material.navigation.NavigationView = activity.findViewById(R.id.navigationView)

    // Mostrar lista
    fun mostrarLista(lista: List<String>) {
        val adapter = ArrayAdapter(listView.context, android.R.layout.simple_list_item_1, lista)
        listView.adapter = adapter
    }

    // Mostrar mensajes toast
    fun mostrarMensaje(msg: String) {
        Toast.makeText(spinnerCombustible.context, msg, Toast.LENGTH_SHORT).show()
    }

    // Cargar datos al spinner
    fun setSpinner(adapter: SpinnerAdapter) {
        spinnerCombustible.adapter = adapter
    }

    // Obtener índice seleccionado
    fun getCombustibleSeleccionado(): Int {
        return spinnerCombustible.selectedItemPosition
    }

    // Eventos
    fun onBtnCargarClick(callback: () -> Unit) {
        btnCargar.setOnClickListener { callback() }
    }

    fun onBtnMenuClick(callback: () -> Unit) {
        btnMenu.setOnClickListener { callback() }
    }

    fun onItemMenuClick(callback: (Int) -> Unit) {
        navigationView.setNavigationItemSelectedListener { item ->
            callback(item.itemId)
            true
        }
    }

    fun abrirDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun cerrarDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun setOnItemSelected(callback: (Int) -> Unit) {
        listView.setOnItemClickListener { _, _, position, _ ->
            callback(position)
        }
    }

}