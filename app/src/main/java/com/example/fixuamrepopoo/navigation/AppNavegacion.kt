package com.example.fixuamrepopoo.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*

import com.example.fixuamrepopoo.screens.*
import com.example.fixuamrepopoo.ui.theme.ConfiguracionTema
import com.example.fixuamrepopoo.ui.theme.LocalConfiguracionTema

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {

    var pantallaActual by remember { mutableStateOf("login") }
    var rolSeleccionado by remember { mutableStateOf("") }
    var modoOscuro by remember { mutableStateOf(false) }

    var usuarioActual by remember { mutableStateOf("") }
    var usuarioActualUid by remember { mutableStateOf("") }

    val reportes = remember {
        mutableStateListOf<Reporte>()
    }

    var reporteTemporal by remember {
        mutableStateOf<Reporte?>(null)
    }

    var reporteSeleccionadoId by remember {
        mutableStateOf<Int?>(null)
    }

    fun irPantallaPrincipalPorRol(rol: String) {
        pantallaActual = when (rol) {
            "docente" -> "inicio_docente"
            "colaborador" -> "colaborador"
            "admin" -> "dashboard"
            else -> "login"
        }
    }

    fun cerrarSesionCompleta() {
        FirebaseServicio.cerrarSesion()

        usuarioActual = ""
        usuarioActualUid = ""
        rolSeleccionado = ""
        reporteTemporal = null
        reporteSeleccionadoId = null
        reportes.clear()
        pantallaActual = "login"
    }

    DisposableEffect(usuarioActualUid, rolSeleccionado) {
        if (usuarioActualUid.isBlank() || rolSeleccionado.isBlank()) {
            onDispose { }
        } else {
            val listener = FirebaseServicio.escucharReportes(
                rol = rolSeleccionado,
                usuarioUid = usuarioActualUid,
                onSuccess = { listaReportes ->
                    reportes.clear()
                    reportes.addAll(listaReportes)
                },
                onError = { mensaje ->
                    println("Error al escuchar reportes: $mensaje")
                }
            )

            onDispose {
                listener.remove()
            }
        }
    }

    CompositionLocalProvider(
        LocalConfiguracionTema provides ConfiguracionTema(
            modoOscuro = modoOscuro,
            cambiarModo = {
                modoOscuro = !modoOscuro
            }
        )
    ) {
        AnimatedContent(
            targetState = pantallaActual,
            transitionSpec = {
                slideInHorizontally(
                    animationSpec = tween(350),
                    initialOffsetX = { fullWidth -> fullWidth }
                ) + fadeIn(
                    animationSpec = tween(350)
                ) togetherWith slideOutHorizontally(
                    animationSpec = tween(350),
                    targetOffsetX = { fullWidth -> -fullWidth }
                ) + fadeOut(
                    animationSpec = tween(350)
                )
            },
            label = "TransicionPantallas"
        ) { pantalla ->

            when (pantalla) {

                "login" -> LoginScreen(
                    seleccionarRol = { rol ->
                        rolSeleccionado = rol
                        pantallaActual = "credenciales"
                    }
                )

                "credenciales" -> CredencialesScreen(
                    rol = rolSeleccionado,
                    volver = {
                        pantallaActual = "login"
                    },
                    ingresar = { correo: String, contrasena: String, mostrarError: (String) -> Unit ->
                        FirebaseServicio.iniciarSesion(
                            correo = correo,
                            contrasena = contrasena,
                            rolSeleccionado = rolSeleccionado,
                            onSuccess = { usuario: Usuario ->
                                usuarioActual = usuario.nombre
                                usuarioActualUid = usuario.uid
                                rolSeleccionado = usuario.rol

                                irPantallaPrincipalPorRol(usuario.rol)
                            },
                            onError = { mensaje: String ->
                                mostrarError(mensaje)
                            }
                        )
                    },
                    crearCuenta = {
                        pantallaActual = "registro"
                    }
                )

                "registro" -> RegistroScreen(
                    rol = rolSeleccionado,
                    volver = {
                        pantallaActual = "credenciales"
                    },
                    registrarUsuario = { nuevoUsuario: Usuario, mostrarError: (String) -> Unit ->
                        FirebaseServicio.registrarUsuario(
                            usuario = nuevoUsuario,
                            onSuccess = { usuarioRegistrado: Usuario ->
                                usuarioActual = usuarioRegistrado.nombre
                                usuarioActualUid = usuarioRegistrado.uid
                                rolSeleccionado = usuarioRegistrado.rol

                                irPantallaPrincipalPorRol(usuarioRegistrado.rol)
                            },
                            onError = { mensaje: String ->
                                mostrarError(mensaje)
                            }
                        )
                    }
                )

                "dashboard" -> DashboardScreen(
                    rol = rolSeleccionado,
                    reportes = reportes,
                    usuarioNombre = usuarioActual,
                    onCerrarSesion = {
                        cerrarSesionCompleta()
                    },
                    onNavigateToInicio = {
                        pantallaActual = "dashboard"
                    },
                    eliminarReporte = { id ->
                        val reporte = reportes.find { it.id == id }

                        if (reporte != null) {
                            FirebaseServicio.eliminarReporte(
                                firestoreId = reporte.firestoreId,
                                onSuccess = {},
                                onError = { mensaje ->
                                    println("Error al eliminar reporte: $mensaje")
                                }
                            )
                        }
                    }
                )

                "inicio_docente" -> InicioDocenteScreen(
                    irNuevoReporte = {
                        pantallaActual = "nuevo_reporte"
                    },
                    irMisReportes = {
                        pantallaActual = "mis_reportes"
                    },
                    irPerfil = {
                        pantallaActual = "perfil_docente"
                    }
                )

                "nuevo_reporte" -> NuevoReporteScreen(
                    volver = {
                        pantallaActual = "inicio_docente"
                    },
                    continuar = { reporte ->
                        reporteTemporal = reporte.copy(
                            docente = usuarioActual,
                            docenteUid = usuarioActualUid,
                            estado = "Pendiente",
                            atendidoPor = "",
                            atendidoPorUid = ""
                        )

                        pantallaActual = "detalle_reporte"
                    }
                )

                "detalle_reporte" -> DetalleReporteScreen(
                    reporte = reporteTemporal,
                    volver = {
                        pantallaActual = "nuevo_reporte"
                    },
                    enviarReporte = {
                        reporteTemporal?.let { reporte ->
                            FirebaseServicio.guardarReporte(
                                reporte = reporte,
                                onSuccess = {
                                    reporteTemporal = null
                                    pantallaActual = "confirmacion"
                                },
                                onError = { mensaje ->
                                    println("Error al guardar reporte: $mensaje")
                                }
                            )
                        }
                    }
                )

                "confirmacion" -> ConfirmacionScreen(
                    irInicio = {
                        pantallaActual = "inicio_docente"
                    },
                    irMisReportes = {
                        pantallaActual = "mis_reportes"
                    }
                )

                "mis_reportes" -> MisReportesScreen(
                    reportes = reportes.filter { it.docenteUid == usuarioActualUid },
                    volver = {
                        pantallaActual = "inicio_docente"
                    },
                    irDetalleEstado = { id ->
                        reporteSeleccionadoId = id
                        pantallaActual = "detalle_estado"
                    }
                )

                "detalle_estado" -> DetalleEstadoScreen(
                    reporte = reportes.find { it.id == reporteSeleccionadoId },
                    volver = {
                        pantallaActual = "mis_reportes"
                    },
                    cancelarReporte = { id ->
                        val reporte = reportes.find { it.id == id }

                        if (reporte != null) {
                            FirebaseServicio.eliminarReporte(
                                firestoreId = reporte.firestoreId,
                                onSuccess = {
                                    pantallaActual = "mis_reportes"
                                },
                                onError = { mensaje ->
                                    println("Error al cancelar reporte: $mensaje")
                                }
                            )
                        } else {
                            pantallaActual = "mis_reportes"
                        }
                    }
                )

                "perfil_docente" -> PerfilDocenteScreen(
                    volver = {
                        pantallaActual = "inicio_docente"
                    },
                    cerrarSesion = {
                        cerrarSesionCompleta()
                    }
                )

                "colaborador" -> ColaboradorScreen(
                    reportes = reportes,
                    colaboradorNombre = usuarioActual.ifBlank { "Soporte UAM" },
                    colaboradorUid = usuarioActualUid,
                    tomarReporte = { id ->
                        val reporte = reportes.find { it.id == id }

                        if (reporte != null) {
                            FirebaseServicio.tomarReporte(
                                firestoreId = reporte.firestoreId,
                                colaboradorUid = usuarioActualUid,
                                colaboradorNombre = usuarioActual.ifBlank { "Soporte UAM" },
                                onSuccess = {},
                                onError = { mensaje ->
                                    println("Error al tomar reporte: $mensaje")
                                }
                            )
                        }
                    },
                    resolverReporte = { id ->
                        val reporte = reportes.find { it.id == id }

                        if (reporte != null) {
                            FirebaseServicio.resolverReporte(
                                firestoreId = reporte.firestoreId,
                                onSuccess = {},
                                onError = { mensaje ->
                                    println("Error al resolver reporte: $mensaje")
                                }
                            )
                        }
                    },
                    cerrarSesion = {
                        cerrarSesionCompleta()
                    }
                )
            }
        }
    }
}