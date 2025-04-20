package com.arquitectura.gasolineraapp.vista.calculo

import android.app.Activity
import android.os.Bundle
import com.arquitectura.gasolineraapp.R
import com.arquitectura.gasolineraapp.controlador.cCalculo

class calculoActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculo)

        val idSucursal = intent.getIntExtra("idSucursal", -1)
        val idSucursalCombustible = intent.getIntExtra("idSucursalCombustible", -1)

        if (idSucursal != -1 && idSucursalCombustible != -1) {
            cCalculo(this, idSucursal, idSucursalCombustible)
        }
    }
}
