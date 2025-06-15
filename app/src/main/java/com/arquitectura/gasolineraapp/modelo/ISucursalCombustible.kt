package com.arquitectura.gasolineraapp.modelo

import android.content.Context

interface ISucursalCombustible {
    fun crear(context: Context, registro: mSucursalCombustible): Boolean
    fun actualizar(context: Context, registro: mSucursalCombustible): Boolean
    fun eliminarPorId(context: Context, id: Int): Boolean
    fun listar(context: Context): List<mSucursalCombustible>
}
