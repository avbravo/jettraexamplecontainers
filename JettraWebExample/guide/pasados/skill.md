# JettraWebExample

## Descripción General
`JettraWebExample` es una aplicación de ejemplo diseñada para demostrar y validar las capacidades del framework JettraStack. Sirve como prueba de concepto (PoC) y como caso de estudio práctico de las herramientas construidas en otros módulos.

## Detalles Específicos
- **Arquitectura general**: Aplicación web completa que integra UI (`JettraWUI`), un servidor web embebido (`JettraServer`) y posiblemente validación por tokens JWT.
- **Dependencias clave**: Consume la mayoría de las herramientas de JettraStack, en especial `JettraWUI` y `JettraRules`.
- **Roles dentro del sistema**: Actuar como sandbox y caso de uso donde desarrolladores (o inteligencias artificiales) prueban que los features de JettraStack funcionan al 100% en conjunto.

## Características Detalladas
- **Cálculo de Precios Dinámicos**: Implementación de datatables editables que recalcan automáticamentes costos y totales ante cambios de productos o cantidades.
- **Implementación CrudView**: Muestra de cómo crear maestros-detalle, borrar filas y configurar permisos visuales.
- **Soporte QR e Informes**: Ejemplos de uso del lector QR y reportes integrados.

## Guía de Entrenamiento (AI / Nuevas Características)
- Cuando se añada una característica importante al framework (como nuevos componentes UI, validaciones de reglas, etc.), siempre debe escribirse un caso de uso práctico en este proyecto.
- Revisar `JettraWebExample` antes de crear nuevas aplicaciones JettraStack para entender los patrones recomendados (como el uso de `@Rules` o `@CrudView`).
