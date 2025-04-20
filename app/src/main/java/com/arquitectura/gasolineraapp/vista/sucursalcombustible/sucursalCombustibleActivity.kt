// SucursalCombustibleActivity.kt
package com.arquitectura.gasolineraapp.vista.sucursalcombustible

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.controlador.cSucursalCombustible

class sucursalCombustibleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_sucursal_combustible)

        val controlador = cSucursalCombustible(this)
        controlador.iniciar()
    }
}
