package com.arquitectura.gasolineraapp.modelo

import android.content.Context
import android.util.Log

class ProxySucursalCombustible : ISucursalCombustible {

    private val real = mSucursalCombustible()  // El proxy crea su modelo real internamente

    private val eliminaciones = mutableListOf<Long>()
    private val creaciones = mutableListOf<Long>()
    private val actualizaciones = mutableListOf<Long>()
    private val ventanaMs = 60_000L

    private val limiteEliminacion = 2
    private val limiteCreacion = 3
    private val limiteActualizacion = 3

    override fun crear(context: Context, registro: mSucursalCombustible): Boolean {
        val ahora = System.currentTimeMillis()
        creaciones.removeAll { ahora - it > ventanaMs }

        if (creaciones.size >= limiteCreacion) {
            Log.w("Proxy SucursalCombustible", "Límite de creaciones alcanzado (${creaciones.size} en menos de 60s)")
            return false
        }

        val resultado = real.crear(context, registro)
        if (resultado) {
            creaciones.add(ahora)
            Log.d("Proxy SucursalCombustible", "Creación registrada. Total recientes: ${creaciones.size}")
        }
        return resultado
    }

    override fun actualizar(context: Context, registro: mSucursalCombustible): Boolean {
        val ahora = System.currentTimeMillis()
        actualizaciones.removeAll { ahora - it > ventanaMs }

        if (actualizaciones.size >= limiteActualizacion) {
            Log.w("Proxy SucursalCombustible", "Límite de actualizaciones alcanzado (${actualizaciones.size} en menos de 60s)")
            return false
        }

        val resultado = real.actualizar(context, registro)
        if (resultado) {
            actualizaciones.add(ahora)
            Log.d("Proxy SucursalCombustible", "Actualización registrada. Total recientes: ${actualizaciones.size}")
        }
        return resultado
    }

    override fun eliminarPorId(context: Context, id: Int): Boolean {
        val ahora = System.currentTimeMillis()
        eliminaciones.removeAll { ahora - it > ventanaMs }

        if (eliminaciones.size >= limiteEliminacion) {
            Log.w("Proxy SucursalCombustible", "Límite de eliminaciones alcanzado (${eliminaciones.size} en menos de 60s)")
            return false
        }

        val resultado = real.eliminarPorId(context, id)
        if (resultado) {
            eliminaciones.add(ahora)
            Log.d("Proxy SucursalCombustible", "Eliminación registrada. Total recientes: ${eliminaciones.size}")
        }
        return resultado
    }

    override fun listar(context: Context): List<mSucursalCombustible> {
        Log.d("Proxy SucursalCombustible", "listar llamado")
        return real.listar(context)
    }
}
