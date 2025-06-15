// Ruta: com/arquitectura/gasolineraapp/vista/disponibilidad/DisponibilidadActivity.kt

package com.arquitectura.gasolineraapp.vista.disponibilidad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.controlador.cDisponibilidad

class disponibilidadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_disponibilidad)
        val controlador = cDisponibilidad(this)
        controlador.iniciar()
    }
}
