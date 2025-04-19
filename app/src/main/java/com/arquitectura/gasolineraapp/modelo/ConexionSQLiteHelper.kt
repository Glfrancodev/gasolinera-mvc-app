package com.arquitectura.gasolineraapp.modelo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionSQLiteHelper(context: Context) : SQLiteOpenHelper(
    context,
    "GasolineraDB", // nombre de la base de datos
    null,
    6  // versión actualizada
) {
    override fun onCreate(db: SQLiteDatabase?) {
        // Tabla Sucursal
        db?.execSQL(
            """
            CREATE TABLE Sucursal (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                direccion TEXT NOT NULL,
                latitud REAL NOT NULL,
                longitud REAL NOT NULL
            )
            """.trimIndent()
        )

        // Tabla Constantes
        db?.execSQL(
            """
            CREATE TABLE Constantes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                valor REAL NOT NULL,
                unidad TEXT NOT NULL
            )
            """.trimIndent()
        )

        // Tabla TipoCombustible
        db?.execSQL(
            """
            CREATE TABLE TipoCombustible (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                descripcion TEXT
            )
            """.trimIndent()
        )

        // Tabla SucursalCombustible (intermedia)
        db?.execSQL(
            """
            CREATE TABLE SucursalCombustible (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                idSucursal INTEGER NOT NULL,
                idCombustible INTEGER NOT NULL,
                cantidadBombas INTEGER NOT NULL,
                fechaMedicion TEXT,
                combustibleDisponible REAL,
                FOREIGN KEY (idSucursal) REFERENCES Sucursal(id),
                FOREIGN KEY (idCombustible) REFERENCES TipoCombustible(id)
            )
            """.trimIndent()
        )

        // Insertar constantes fijas
        db?.execSQL("INSERT INTO Constantes (nombre, valor, unidad) VALUES ('Tiempo promedio de carga por auto', 3, 'minutos')")
        db?.execSQL("INSERT INTO Constantes (nombre, valor, unidad) VALUES ('Cantidad promedio de litros por auto', 10, 'litros')")
        db?.execSQL("INSERT INTO Constantes (nombre, valor, unidad) VALUES ('Longitud promedio de un auto', 5, 'metros')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS SucursalCombustible")
        db?.execSQL("DROP TABLE IF EXISTS TipoCombustible")
        db?.execSQL("DROP TABLE IF EXISTS Constantes")
        db?.execSQL("DROP TABLE IF EXISTS Sucursal")
        onCreate(db)
    }
}
