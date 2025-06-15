package com.arquitectura.gasolineraapp.vista.sucursal

import android.app.Activity
import android.widget.*
import androidx.core.view.GravityCompat
import com.arquitectura.gasolineraapp.R

class vSucursal(activity: Activity) {

    private val etNombre: EditText = activity.findViewById(R.id.etNombre)
    private val etDireccion: EditText = activity.findViewById(R.id.etDireccion)
    val btnGuardar: Button = activity.findViewById(R.id.btnGuardar)
    val listView: ListView = activity.findViewById(R.id.listSucursales)
    val btnMenu: ImageButton = activity.findViewById(R.id.btnMenu)
    val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = activity.findViewById(R.id.drawerLayout)
    val navigationView: com.google.android.material.navigation.NavigationView = activity.findViewById(R.id.navigationView)

    fun getNombre(): String = etNombre.text.toString()
    fun getDireccion(): String = etDireccion.text.toString()
    fun setNombre(nombre: String) { etNombre.setText(nombre) }
    fun setDireccion(direccion: String) { etDireccion.setText(direccion) }
    fun setModoActualizar() { btnGuardar.text = "Actualizar" }
    fun setModoCrear() { btnGuardar.text = "Guardar" }

    fun limpiarCampos() {
        etNombre.setText("")
        etDireccion.setText("")
        setModoCrear()
    }

    fun mostrarMensaje(mensaje: String) {
        Toast.makeText(etNombre.context, mensaje, Toast.LENGTH_SHORT).show()
    }

    fun mostrarLista(lista: List<String>) {
        val adapter = ArrayAdapter(etNombre.context, android.R.layout.simple_list_item_1, lista)
        listView.adapter = adapter
    }

    fun onBtnGuardarClick(callback: () -> Unit) {
        btnGuardar.setOnClickListener { callback() }
    }

    fun onBtnMenuClick(callback: () -> Unit) {
        btnMenu.setOnClickListener { callback() }
    }

    fun onItemSeleccionado(callback: (Int) -> Unit) {
        listView.setOnItemClickListener { _, _, position, _ -> callback(position) }
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
