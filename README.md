# FixUAM - Sistema Móvil de Reportes Tecnológicos

FixUAM es una aplicación móvil desarrollada en Android Studio con Kotlin y Jetpack Compose. Su objetivo principal es permitir que los docentes de la UAM reporten de manera rápida y organizada problemas tecnológicos dentro del aula, como fallas en computadoras, proyectores, pantallas interactivas, cables HDMI, conexión a internet o accesorios.

La aplicación busca mejorar la comunicación entre los docentes y la Dirección Tecnológica, evitando el uso de llamadas personales o mensajes informales. De esta manera, los reportes se registran dentro de la app y pueden ser atendidos por el personal técnico según el orden de llegada.

---

## Descripción del proyecto

En muchas ocasiones, cuando ocurre una falla tecnológica dentro del aula, el docente debe buscar a una persona de soporte, llamar a un número personal o esperar a que alguien esté disponible. Esto puede retrasar la clase y afectar el desarrollo normal de las actividades académicas.

FixUAM propone una solución digital donde el docente puede ingresar a la aplicación, seleccionar el tipo de problema, indicar el aula donde ocurre la falla, describir la incidencia y adjuntar una foto como evidencia. Posteriormente, el reporte aparece en el panel del colaborador técnico, quien puede tomar el caso, atenderlo y marcarlo como solucionado.

---

## Objetivo general

Desarrollar una aplicación móvil que permita reportar, visualizar y gestionar incidencias tecnológicas dentro de las aulas de la UAM, facilitando la comunicación entre docentes y personal de soporte técnico.

---

## Objetivos específicos

- Permitir que los docentes reporten fallas tecnológicas desde una aplicación móvil.
- Registrar información básica del problema, como tipo de incidencia, aula, descripción y evidencia fotográfica.
- Mostrar los reportes enviados en un panel para colaboradores técnicos.
- Permitir que el colaborador tome un reporte y cambie su estado.
- Reflejar el estado del reporte en la vista del docente.
- Implementar una interfaz moderna, clara y fácil de utilizar.
- Aplicar principios básicos de programación orientada a objetos mediante modelos de datos.

---

## Roles de usuario

La aplicación contempla tres tipos de usuarios:

### Docente

El docente puede:

- Iniciar sesión con correo y contraseña.
- Crear un nuevo reporte tecnológico.
- Seleccionar el tipo de problema.
- Escribir el salón o aula donde ocurre la falla.
- Describir el problema.
- Adjuntar o tomar una foto del problema.
- Consultar sus reportes enviados.
- Ver si el reporte está pendiente, en proceso o resuelto.
- Cancelar un reporte si aún no ha sido solucionado.
- Cerrar sesión.

### Colaborador técnico

El colaborador técnico puede:

- Iniciar sesión con correo y contraseña.
- Ver los reportes pendientes enviados por docentes.
- Tomar un reporte para atenderlo.
- Ver los reportes asignados en la sección de tareas.
- Marcar un reporte como solucionado.
- Cerrar sesión.

### Administrador

El rol de administrador está contemplado dentro del diseño general de la aplicación. En esta versión inicial, el acceso de administrador puede reutilizar el panel del colaborador, pero se deja preparado para futuras mejoras como gestión de usuarios, reportes generales o estadísticas.

---

## Funcionalidades principales

- Pantalla de selección de rol.
- Pantalla de ingreso con correo y contraseña.
- Panel principal para docentes.
- Formulario para crear reportes.
- Selección del tipo de problema tecnológico.
- Registro del aula donde ocurre la incidencia.
- Descripción detallada del problema.
- Adjuntar imagen desde galería.
- Tomar foto directamente desde la cámara.
- Vista previa del detalle del reporte antes de enviarlo.
- Confirmación de reporte enviado.
- Historial de reportes del docente.
- Detalle del estado del reporte.
- Panel de colaborador técnico.
- Toma de reportes pendientes.
- Cambio de estado de reportes.
- Cancelación de reportes.
- Cierre de sesión.
- Modo claro y modo oscuro.
- Fondo animado en la pantalla inicial.
- Transiciones entre pantallas.

---

## Tecnologías utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material 3
- Programación Orientada a Objetos
- Git y GitHub

---

## Arquitectura general del proyecto

El proyecto está organizado en paquetes para separar responsabilidades y facilitar el mantenimiento del código.

```plaintext
com.example.proyecto_fixuam
│
├── model
│   └── Reporte.kt
│
├── navigation
│   └── AppNavigation.kt
│
├── screens
│   ├── ColaboradorScreen.kt
│   ├── Componentes.kt
│   ├── ConfirmacionScreen.kt
│   ├── CredencialesScreen.kt
│   ├── DetalleEstadoScreen.kt
│   ├── DetalleReporteScreen.kt
│   ├── InicioDocenteScreen.kt
│   ├── LoginScreen.kt
│   ├── MisReportesScreen.kt
│   ├── NuevoReporteScreen.kt
│   └── PerfilDocenteScreen.kt
│
├── ui.theme
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
│
└── MainActivity.kt
