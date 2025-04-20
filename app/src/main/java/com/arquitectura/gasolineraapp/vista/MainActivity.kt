package com.arquitectura.gasolineraapp.vista

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.controlador.cDisponibilidad

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aquí sí mostramos el layout primero
        setContentView(R.layout.activity_v_disponibilidad)

        // Y luego pasamos el contexto ya con vistas listas
        val controlador = cDisponibilidad(this)
        controlador.iniciar()
    }
}