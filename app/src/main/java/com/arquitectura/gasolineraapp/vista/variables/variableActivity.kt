// ConstanteActivity.kt
package com.arquitectura.gasolineraapp.vista.variables

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.controlador.cConstante

class variableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_variable)

        val controlador = cConstante(this)
        controlador.iniciar()
    }
}
