package com.arquitectura.gasolineraapp.vista.constante

import android.app.Activity
import android.widget.*
import androidx.core.view.GravityCompat
import com.arquitectura.gasolineraapp.R

class vConstanteActivity(activity: Activity) {

    // Campos visuales
    val tvId: TextView = activity.findViewById(R.id.tvId)
    val tvNombre: TextView = activity.findViewById(R.id.tvNombre)
    val tvUnidad: TextView = activity.findViewById(R.id.tvUnidad)
    val etValor: EditText = activity.findViewById(R.id.etValor)

    val btnGuardar: Button = activity.findViewById(R.id.btnGuardar)
    val listView: ListView = activity.findViewById(R.id.listConstantes)

    // Menú lateral
    val btnMenu: ImageButton = activity.findViewById(R.id.btnMenu)
    val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = activity.findViewById(R.id.drawerLayout)
    val navigationView: com.google.android.material.navigation.NavigationView = activity.findViewById(R.id.navigationView)

    // Mostrar mensaje
    fun mostrarMensaje(mensaje: String) {
        Toast.makeText(etValor.context, mensaje, Toast.LENGTH_SHORT).show()
    }

    // Mostrar lista
    fun mostrarLista(adapter: ListAdapter) {
        listView.adapter = adapter
    }

    // Cargar datos seleccionados
    fun setDatos(id: Int, nombre: String, unidad: String, valor: Double) {
        tvId.text = id.toString()
        tvNombre.text = nombre
        tvUnidad.text = unidad
        etValor.setText(valor.toString())
    }

    // Limpiar campos
    fun limpiarCampos() {
        tvId.text = ""
        tvNombre.text = ""
        tvUnidad.text = ""
        etValor.setText("")
    }

    // Acciones de botones
    fun onBtnGuardarClick(callback: () -> Unit) {
        btnGuardar.setOnClickListener { callback() }
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

    fun onItemListaClick(callback: (Int) -> Unit) {
        listView.setOnItemClickListener { _, _, position, _ ->
            callback(position)
        }
    }

    // Abrir y cerrar menú
    fun abrirDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun cerrarDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }
}
