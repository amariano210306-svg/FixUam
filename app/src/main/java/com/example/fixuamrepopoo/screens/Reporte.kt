package com.example.fixuamrepopoo.screens

import android.graphics.Bitmap

data class Reporte(
    val id: Int = 0,
    val firestoreId: String = "",
    val docenteUid: String = "",
    val docente: String = "",
    val tipo: String = "",
    val aula: String = "",
    val descripcion: String = "",
    val fecha: String = "",
    val prioridad: String = "Media",
    val estado: String = "Pendiente",
    val atendidoPor: String = "",
    val atendidoPorUid: String = "",
    val fotoUri: String = "",
    val fotoBitmap: Bitmap? = null
)