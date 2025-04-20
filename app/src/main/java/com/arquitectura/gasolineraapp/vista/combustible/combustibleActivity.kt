// TipoCombustibleActivity.kt
package com.arquitectura.gasolineraapp.vista.combustible

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.controlador.cTipoCombustible

class combustibleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_tipo_combustible)

        val controlador = cTipoCombustible(this)
        controlador.iniciar()
    }
}
