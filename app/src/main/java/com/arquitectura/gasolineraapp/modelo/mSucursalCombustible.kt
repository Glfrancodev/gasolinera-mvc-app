package com.arquitectura.gasolineraapp.modelo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class mSucursalCombustible(
    var id: Int = 0,
    var idSucursal: Int = 0,
    var idCombustible: Int = 0,
    var cantidadBombas: Int = 0,
    var horaMedicion: String = "",
    var combustibleDisponible: Double = 0.0
) : ISucursalCombustible {

    override fun crear(context: Context, registro: mSucursalCombustible): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val valores = ContentValues().apply {
            put("idSucursal", registro.idSucursal)
            put("idCombustible", registro.idCombustible)
            put("cantidadBombas", registro.cantidadBombas)
            put("horaMedicion", registro.horaMedicion)
            put("combustibleDisponible", registro.combustibleDisponible)
        }
        val resultado = db.insert("SucursalCombustible", null, valores)
        db.close()
        return resultado != -1L
    }

    override fun actualizar(context: Context, registro: mSucursalCombustible): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val valores = ContentValues().apply {
            put("idSucursal", registro.idSucursal)
            put("idCombustible", registro.idCombustible)
            put("cantidadBombas", registro.cantidadBombas)
            put("combustibleDisponible", registro.combustibleDisponible)
        }
        val resultado = db.update("SucursalCombustible", valores, "id=?", arrayOf(registro.id.toString()))
        db.close()
        return resultado > 0
    }

    override fun eliminarPorId(context: Context, id: Int): Boolean {
        val db = ConexionSQLiteHelper(context).writableDatabase
        val resultado = db.delete("SucursalCombustible", "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    override fun listar(context: Context): List<mSucursalCombustible> {
        val lista = mutableListOf<mSucursalCombustible>()
        val db = ConexionSQLiteHelper(context).readableDatabase
        val cursor = db.rawQuery("SELECT * FROM SucursalCombustible", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    mSucursalCombustible(
                        id = cursor.getInt(0),
                        idSucursal = cursor.getInt(1),
                        idCombustible = cursor.getInt(2),
                        cantidadBombas = cursor.getInt(3),
                        horaMedicion = cursor.getString(4),
                        combustibleDisponible = cursor.getDouble(5)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}
