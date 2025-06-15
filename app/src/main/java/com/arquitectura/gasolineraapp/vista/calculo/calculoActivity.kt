// Ruta: com/arquitectura/gasolineraapp/vista/calculo/calculoActivity.kt
package com.arquitectura.gasolineraapp.vista.calculo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.controlador.cCalculo

class calculoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_calculo)
        val controlador = cCalculo(this)
        controlador.iniciar()
    }
}
