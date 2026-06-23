package com.example.fixuamrepopoo.screens

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

object FirebaseServicio {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun registrarUsuario(
        usuario: Usuario,
        onSuccess: (Usuario) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(usuario.correo, usuario.contrasena)
            .addOnSuccessListener { resultado ->
                val uid = resultado.user?.uid

                if (uid == null) {
                    onError("No se pudo obtener el ID del usuario")
                    return@addOnSuccessListener
                }

                val datosUsuario = hashMapOf(
                    "uid" to uid,
                    "nombre" to usuario.nombre,
                    "cif" to usuario.cif,
                    "facultad" to usuario.facultad,
                    "correo" to usuario.correo,
                    "rol" to usuario.rol
                )

                db.collection("usuarios")
                    .document(uid)
                    .set(datosUsuario)
                    .addOnSuccessListener {
                        onSuccess(
                            usuario.copy(
                                uid = uid,
                                contrasena = ""
                            )
                        )
                    }
                    .addOnFailureListener { error ->
                        onError(error.message ?: "Error al guardar usuario")
                    }
            }
            .addOnFailureListener { error ->
                onError(error.message ?: "Error al crear cuenta")
            }
    }

    fun iniciarSesion(
        correo: String,
        contrasena: String,
        rolSeleccionado: String,
        onSuccess: (Usuario) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(correo, contrasena)
            .addOnSuccessListener { resultado ->
                val uid = resultado.user?.uid

                if (uid == null) {
                    onError("No se pudo obtener el usuario")
                    return@addOnSuccessListener
                }

                db.collection("usuarios")
                    .document(uid)
                    .get()
                    .addOnSuccessListener { documento ->
                        if (!documento.exists()) {
                            onError("El usuario no tiene datos registrados")
                            return@addOnSuccessListener
                        }

                        val rol = documento.getString("rol") ?: ""

                        if (rol != rolSeleccionado) {
                            auth.signOut()
                            onError("Esta cuenta no pertenece al rol seleccionado")
                            return@addOnSuccessListener
                        }

                        val usuario = Usuario(
                            uid = uid,
                            nombre = documento.getString("nombre") ?: "",
                            cif = documento.getString("cif") ?: "",
                            facultad = documento.getString("facultad") ?: "",
                            correo = documento.getString("correo") ?: "",
                            rol = rol
                        )

                        onSuccess(usuario)
                    }
                    .addOnFailureListener { error ->
                        onError(error.message ?: "Error al leer datos del usuario")
                    }
            }
            .addOnFailureListener {
                onError("Correo o contraseña incorrectos")
            }
    }

    fun guardarReporte(
        reporte: Reporte,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val documento = db.collection("reportes").document()

        val datosReporte = hashMapOf(
            "id" to reporte.id,
            "firestoreId" to documento.id,
            "docenteUid" to reporte.docenteUid,
            "docente" to reporte.docente,
            "tipo" to reporte.tipo,
            "aula" to reporte.aula,
            "descripcion" to reporte.descripcion,
            "fecha" to reporte.fecha,
            "prioridad" to reporte.prioridad,
            "estado" to reporte.estado,
            "atendidoPor" to reporte.atendidoPor,
            "atendidoPorUid" to reporte.atendidoPorUid,
            "fechaOrden" to System.currentTimeMillis()
        )

        documento
            .set(datosReporte)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { error ->
                onError(error.message ?: "Error al guardar reporte")
            }
    }

    fun escucharReportes(
        rol: String,
        usuarioUid: String,
        onSuccess: (List<Reporte>) -> Unit,
        onError: (String) -> Unit
    ): ListenerRegistration {
        val consulta = if (rol == "docente") {
            db.collection("reportes")
                .whereEqualTo("docenteUid", usuarioUid)
        } else {
            db.collection("reportes")
        }

        return consulta.addSnapshotListener { resultado, error ->
            if (error != null) {
                onError(error.message ?: "Error al escuchar reportes")
                return@addSnapshotListener
            }

            if (resultado == null) {
                onSuccess(emptyList())
                return@addSnapshotListener
            }

            val listaReportes = resultado.documents.map { documento ->
                Reporte(
                    id = documento.getLong("id")?.toInt() ?: 0,
                    firestoreId = documento.id,
                    docenteUid = documento.getString("docenteUid") ?: "",
                    docente = documento.getString("docente") ?: "",
                    tipo = documento.getString("tipo") ?: "",
                    aula = documento.getString("aula") ?: "",
                    descripcion = documento.getString("descripcion") ?: "",
                    fecha = documento.getString("fecha") ?: "",
                    prioridad = documento.getString("prioridad") ?: "Media",
                    estado = documento.getString("estado") ?: "Pendiente",
                    atendidoPor = documento.getString("atendidoPor") ?: "",
                    atendidoPorUid = documento.getString("atendidoPorUid") ?: ""
                )
            }.sortedByDescending { it.id }

            onSuccess(listaReportes)
        }
    }

    fun tomarReporte(
        firestoreId: String,
        colaboradorUid: String,
        colaboradorNombre: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (firestoreId.isBlank()) {
            onError("No se encontró el reporte")
            return
        }

        db.collection("reportes")
            .document(firestoreId)
            .update(
                mapOf(
                    "estado" to "En proceso",
                    "atendidoPor" to colaboradorNombre,
                    "atendidoPorUid" to colaboradorUid
                )
            )
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { error ->
                onError(error.message ?: "Error al tomar reporte")
            }
    }

    fun resolverReporte(
        firestoreId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (firestoreId.isBlank()) {
            onError("No se encontró el reporte")
            return
        }

        db.collection("reportes")
            .document(firestoreId)
            .update("estado", "Resuelto")
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { error ->
                onError(error.message ?: "Error al resolver reporte")
            }
    }

    fun eliminarReporte(
        firestoreId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (firestoreId.isBlank()) {
            onError("No se encontró el reporte")
            return
        }

        db.collection("reportes")
            .document(firestoreId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { error ->
                onError(error.message ?: "Error al eliminar reporte")
            }
    }

    fun cerrarSesion() {
        auth.signOut()
    }
}
