package com.example.fixuamrepopoo.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixuamrepopoo.ui.theme.ColorPrincipal
import com.example.fixuamrepopoo.ui.theme.ColorRojo
import com.example.fixuamrepopoo.ui.theme.LocalConfiguracionTema
import com.example.fixuamrepopoo.ui.theme.tarjetaApp
import com.example.fixuamrepopoo.ui.theme.textoApp
import com.example.fixuamrepopoo.ui.theme.textoSecundarioApp

@Composable
fun RegistroScreen(
    rol: String,
    volver: () -> Unit,
    registrarUsuario: (Usuario, (String) -> Unit) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var cif by remember { mutableStateOf("") }
    var facultad by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    val config = LocalConfiguracionTema.current

    FondoDegradadoAnimado {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(42.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(34.dp),
                colors = CardDefaults.cardColors(
                    containerColor = tarjetaApp(config.modoOscuro)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "< Volver",
                        color = ColorPrincipal,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .clickable {
                                if (!cargando) {
                                    volver()
                                }
                            }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Crear cuenta",
                        fontSize = 31.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorPrincipal
                    )

                    Text(
                        text = "Registro como ${rol.replaceFirstChar { it.uppercase() }}",
                        color = textoSecundarioApp(config.modoOscuro)
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            error = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nombre completo") },
                        placeholder = { Text("Ej: María Pérez") },
                        singleLine = true,
                        enabled = !cargando,
                        shape = RoundedCornerShape(18.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = cif,
                        onValueChange = {
                            cif = it
                            error = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("CIF o código institucional") },
                        placeholder = { Text("Ej: 202400123") },
                        singleLine = true,
                        enabled = !cargando,
                        shape = RoundedCornerShape(18.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = facultad,
                        onValueChange = {
                            facultad = it
                            error = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Facultad") },
                        placeholder = { Text("Ej: Ingeniería") },
                        singleLine = true,
                        enabled = !cargando,
                        shape = RoundedCornerShape(18.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            error = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Correo institucional") },
                        placeholder = { Text("usuario@uam.edu.ni") },
                        singleLine = true,
                        enabled = !cargando,
                        shape = RoundedCornerShape(18.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = {
                            contrasena = it
                            error = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Contraseña") },
                        singleLine = true,
                        enabled = !cargando,
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(18.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = confirmarContrasena,
                        onValueChange = {
                            confirmarContrasena = it
                            error = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Confirmar contraseña") },
                        singleLine = true,
                        enabled = !cargando,
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(18.dp)
                    )

                    if (error.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = error,
                            color = ColorRojo,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    BotonPrincipal(
                        texto = if (cargando) "Creando cuenta..." else "Crear cuenta",
                        onClick = {
                            if (!cargando) {
                                error = when {
                                    nombre.isBlank() -> "Ingresá tu nombre completo"
                                    cif.isBlank() -> "Ingresá tu CIF o código institucional"
                                    facultad.isBlank() -> "Ingresá tu facultad"
                                    correo.isBlank() -> "Ingresá tu correo institucional"
                                    !correo.contains("@") -> "Ingresá un correo válido"
                                    contrasena.isBlank() -> "Ingresá una contraseña"
                                    contrasena.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
                                    contrasena != confirmarContrasena -> "Las contraseñas no coinciden"
                                    else -> ""
                                }

                                if (error.isEmpty()) {
                                    cargando = true

                                    val nuevoUsuario = Usuario(
                                        nombre = nombre.trim(),
                                        cif = cif.trim(),
                                        facultad = facultad.trim(),
                                        correo = correo.trim(),
                                        contrasena = contrasena,
                                        rol = rol
                                    )

                                    registrarUsuario(nuevoUsuario) { mensajeError ->
                                        error = mensajeError
                                        cargando = false
                                    }
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Tu cuenta quedará asociada al rol seleccionado.",
                        color = textoApp(config.modoOscuro),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

