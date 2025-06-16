package com.arquitectura.gasolineraapp.modelo.proxy

import android.content.Context
import com.arquitectura.gasolineraapp.modelo.mSucursalCombustible

interface ISucursalCombustible {
    fun crear(context: Context, registro: mSucursalCombustible): Boolean
    fun actualizar(context: Context, registro: mSucursalCombustible): Boolean
    fun eliminarPorId(context: Context, id: Int): Boolean
    fun listar(context: Context): List<mSucursalCombustible>
}
