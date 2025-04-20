package com.arquitectura.gasolineraapp.vista.disponibilidad

import android.app.Activity
import android.widget.*
import androidx.core.view.GravityCompat
import com.arquitectura.gasolineraapp.R

class vDisponibilidadActivity(activity: Activity) {
    val spinnerCombustible: Spinner = activity.findViewById(R.id.spinnerCombustible)
    val btnCargar: Button = activity.findViewById(R.id.btnCargar)
    val listView: ListView = activity.findViewById(R.id.listSucursales)
    val btnMenu: ImageButton = activity.findViewById(R.id.btnMenu)
    val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = activity.findViewById(R.id.drawerLayout)
    val navigationView: com.google.android.material.navigation.NavigationView = activity.findViewById(R.id.navigationView)

    fun mostrarLista(adapter: ListAdapter) {
        listView.adapter = adapter
    }

    fun mostrarMensaje(msg: String) {
        Toast.makeText(listView.context, msg, Toast.LENGTH_SHORT).show()
    }

    fun setSpinner(adapter: SpinnerAdapter) {
        spinnerCombustible.adapter = adapter
    }

    fun getCombustibleSeleccionado(): Int {
        return spinnerCombustible.selectedItemPosition
    }

    fun onBtnCargarClick(callback: () -> Unit) {
        btnCargar.setOnClickListener { callback() }
    }

    fun onBtnMenuClick(callback: () -> Unit) {
        btnMenu.setOnClickListener { callback() }
    }

    fun onItemMenuClick(callback: (Int) -> Unit) {
        navigationView.setNavigationItemSelectedListener {
            callback(it.itemId)
            true
        }
    }

    fun abrirDrawer() = drawerLayout.openDrawer(GravityCompat.START)
    fun cerrarDrawer() = drawerLayout.closeDrawer(GravityCompat.START)
}
