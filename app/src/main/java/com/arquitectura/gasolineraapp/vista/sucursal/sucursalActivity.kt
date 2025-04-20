// SucursalActivity.kt
package com.arquitectura.gasolineraapp.vista.sucursal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.controlador.cSucursal

class sucursalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_sucursal)

        val controlador = cSucursal(this)
        controlador.iniciar()
    }
}
