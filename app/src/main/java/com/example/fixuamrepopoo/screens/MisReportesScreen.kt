package com.example.fixuamrepopoo.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fixuamrepopoo.ui.theme.ColorGrisTexto
import com.example.fixuamrepopoo.ui.theme.ColorPrincipal
import com.example.fixuamrepopoo.ui.theme.ColorTexto

@Composable
fun MisReportesScreen(
    reportes: List<Reporte>,
    volver: () -> Unit,
    irDetalleEstado: (Int) -> Unit
) {
    val reportesOrdenados = reportes.sortedByDescending { it.id }

    FondoPrincipal {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp)
        ) {
            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "< Volver",
                color = ColorPrincipal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    volver()
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Mis reportes",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTexto
            )

            Text(
                text = "Consultá el estado de tus reportes enviados",
                color = ColorGrisTexto
            )

            Spacer(modifier = Modifier.height(18.dp))

            TarjetaBlanca(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Total enviados: ${reportesOrdenados.size}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTexto
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Pendientes: ${reportesOrdenados.count { it.estado == "Pendiente" }}",
                    color = ColorGrisTexto
                )

                Text(
                    text = "En proceso: ${reportesOrdenados.count { it.estado == "En proceso" }}",
                    color = ColorGrisTexto
                )

                Text(
                    text = "Resueltos: ${reportesOrdenados.count { it.estado == "Resuelto" }}",
                    color = ColorGrisTexto
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                if (reportesOrdenados.isEmpty()) {
                    TarjetaBlanca(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Aún no has enviado reportes.",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTexto
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Cuando enviés un reporte, aparecerá aquí con su estado actualizado en tiempo real.",
                            color = ColorGrisTexto
                        )
                    }
                } else {
                    reportesOrdenados.forEach { reporte ->
                        OpcionMenu(
                            titulo = reporte.tipo,
                            subtitulo = "${reporte.aula} · ${reporte.estado} · ${reporte.fecha}",
                            letra = reporte.tipo.firstOrNull()?.toString() ?: "R",
                            onClick = {
                                irDetalleEstado(reporte.id)
                            }
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            }
        }
    }
}
