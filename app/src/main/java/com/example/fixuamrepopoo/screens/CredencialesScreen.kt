package com.example.fixuamrepopoo.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.fixuamrepopoo.ui.theme.textoSecundarioApp

@Composable
fun CredencialesScreen(
    rol: String,
    volver: () -> Unit,
    ingresar: (String, String, (String) -> Unit) -> Unit,
    crearCuenta: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    val config = LocalConfiguracionTema.current

    FondoDegradadoAnimado {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.88f),
            shape = RoundedCornerShape(36.dp),
            colors = CardDefaults.cardColors(
                containerColor = tarjetaApp(config.modoOscuro)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 12.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(30.dp),
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
                    text = "FixUAM",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorPrincipal
                )

                Text(
                    text = "Ingreso como ${rol.replaceFirstChar { it.uppercase() }}",
                    color = textoSecundarioApp(config.modoOscuro)
                )

                Spacer(modifier = Modifier.height(28.dp))

                OutlinedTextField(
                    value = correo,
                    onValueChange = {
                        correo = it
                        error = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Correo institucional")
                    },
                    placeholder = {
                        Text("usuario@uam.edu.ni")
                    },
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
                    label = {
                        Text("Contraseña")
                    },
                    singleLine = true,
                    enabled = !cargando,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(18.dp)
                )

                if (error.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = error,
                        color = ColorRojo,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                BotonPrincipal(
                    texto = if (cargando) "Ingresando..." else "Ingresar",
                    onClick = {
                        if (!cargando) {
                            error = when {
                                correo.isBlank() -> "Ingresá tu correo"
                                !correo.contains("@") -> "Ingresá un correo válido"
                                contrasena.isBlank() -> "Ingresá tu contraseña"
                                else -> ""
                            }

                            if (error.isEmpty()) {
                                cargando = true

                                ingresar(
                                    correo.trim(),
                                    contrasena
                                ) { mensajeError ->
                                    error = mensajeError
                                    cargando = false
                                }
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                BotonOscuro(
                    texto = "Crear cuenta",
                    onClick = {
                        if (!cargando) {
                            crearCuenta()
                        }
                    }
                )
            }
        }
    }
}