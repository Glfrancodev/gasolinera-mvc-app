package com.arquitectura.gasolineraapp.vista.sucursalcombustible

import android.app.Activity
import android.widget.*
import androidx.core.view.GravityCompat
import com.arquitectura.gasolineraapp.R

class vSucursalCombustibleActivity(activity: Activity) {

    val spinnerSucursal: Spinner = activity.findViewById(R.id.spinnerSucursal)
    val spinnerCombustible: Spinner = activity.findViewById(R.id.spinnerCombustible)
    val etCantidad: EditText = activity.findViewById(R.id.etCantidad)
    val btnGuardar: Button = activity.findViewById(R.id.btnGuardar)
    val listView: ListView = activity.findViewById(R.id.listSucursalCombustible)
    val btnMenu: ImageButton = activity.findViewById(R.id.btnMenu)
    val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = activity.findViewById(R.id.drawerLayout)
    val navigationView: com.google.android.material.navigation.NavigationView = activity.findViewById(R.id.navigationView)

    fun mostrarMensaje(mensaje: String) {
        Toast.makeText(etCantidad.context, mensaje, Toast.LENGTH_SHORT).show()
    }

    fun mostrarLista(lista: List<String>) {
        val adapter = ArrayAdapter(etCantidad.context, android.R.layout.simple_list_item_1, lista)
        listView.adapter = adapter
    }

    fun setSpinnerSucursal(adapter: SpinnerAdapter) {
        spinnerSucursal.adapter = adapter
    }

    fun setSpinnerCombustible(adapter: SpinnerAdapter) {
        spinnerCombustible.adapter = adapter
    }

    fun setModoActualizar() {
        btnGuardar.text = "Actualizar"
    }

    fun setModoCrear() {
        btnGuardar.text = "Guardar"
    }

    fun limpiarCampos() {
        spinnerSucursal.setSelection(0)
        spinnerCombustible.setSelection(0)
        etCantidad.setText("")
        setModoCrear()
    }

    fun onBtnGuardarClick(callback: () -> Unit) {
        btnGuardar.setOnClickListener { callback() }
    }

    fun onItemSeleccionado(callback: (Int) -> Unit) {
        listView.setOnItemClickListener { _, _, position, _ -> callback(position) }
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
}
