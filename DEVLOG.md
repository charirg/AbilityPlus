# Diario de desarrollo - AbilityPlus (TFC)

---

## Entrada 1
- **Fecha:** 2026-01-19 (lunes)
- **Commit:** Base funcional: Room + CRUD Persona
- **Zona tocada:** Room, arquitectura base

### Qué hice
- Creé la base funcional del CRUD de Persona
- Configuré Room
- Preparé la estructura base del proyecto

### Por qué
- Necesitaba una base sólida sobre la que construir toda la app

### Cómo lo hice
- Creando entidades, DAO y repositorio
- Conectando la base de datos a la app

### Problemas / bloqueos
- Curva de aprendizaje de Room y estructura

### Solución / decisión tomada
- Separar bien capas y hacerlo poco a poco

### Pendiente
- Mejorar la UI
- Implementar más funcionalidades

---

## Entrada 2
- **Fecha:** 2026-01-22 (martes)
- **Commit:** feat: CRUD Persona (Room + MVVM) con borrado lógico y restaurar
- **Zona tocada:** Room, MVVM, lógica de negocio

### Qué hice
- Implementé el borrado lógico de personas
- Añadí la opción de restaurar personas borradas
- Ajusté la lógica del ViewModel y repositorio

### Por qué
- No quería eliminar datos definitivamente
- Quería poder recuperar personas eliminadas

### Cómo lo hice
- Añadiendo un campo de estado (activo/inactivo)
- Filtrando consultas
- Añadiendo acciones de restaurar

### Problemas / bloqueos
- Dudas sobre cómo gestionar el estado de borrado

### Solución / decisión tomada
- Usar borrado lógico en lugar de físico

### Pendiente
- Mejorar experiencia de usuario
- Pulir diseño
- Añadir validaciones

---
## Entrada 3
- **Fecha:** 2026-01-28 (martes)
- **Commit:** feat: persona crud + validations (expediente, fecha, edad)
- **Zona tocada:** Persona, Room, MVVM, validaciones, UI

### Qué hice
- Implementé CRUD completo de Persona:
  - Crear
  - Editar
  - Borrado lógico
  - Restaurar
  - Listado de activas y eliminadas
- Añadí validaciones en el formulario.
- Mejoré la experiencia de usuario al guardar.

### Arquitectura aplicada
- Room (Entity, Dao, Database)
- Repository
- ViewModel
- Jetpack Compose

Se mantiene separación:
UI → ViewModel → Repository → Dao → Base de datos.

### Campos actuales de Persona
- id (PK autogenerada)
- numeroExpediente (String, único)
- sexo (M/F/X)
- edadValoracion (Int?)
- fechaNacimientoMillis (Long?)
- activo (Boolean)

### Validaciones
- Número de expediente obligatorio (ViewModel).
- Formato de fecha YYYY-MM-DD (formulario).
- En edición se precargan los datos.
- Si no se modifica la fecha, se conserva la existente.

### Conversión de fechas
- Texto → LocalDate → epoch millis (Long).
- En listado: millis → dd/MM/yyyy.

### Mejora de UX
- Mensajes de error visibles.
- Los datos no se pierden si hay error.
- El formulario solo se cierra con datos válidos.

### Problemas / bloqueos
- Dudas sobre gestión de fechas y validaciones.

### Solución / decisión tomada
- Centralizar validaciones.
- Usar borrado lógico.
- Trabajar siempre con millis en base de datos.

### Pendiente
- Tablas semilla.
- Relaciones con valoraciones.
- Pulir UI.

---
## Entrada
- **Fecha:** 2026-02-01
- **Commit:** feat: navegación estable + perfil funcional persistente (CIE/CIF/AVD)
- **Zona tocada:** navegación, Room, persona, perfil funcional

### Qué hice
- Navegación coherente: login falso → expedientes (Room) → datos personales → perfil funcional → volver.
- CRUD de Persona funcionando (datos personales).
- Perfil funcional por persona persistente:
    - CIE guardado y marcado visualmente.
    - CIF guardado y marcado; límite de 5 y aviso en rojo al intentar seleccionar una 6ª.
    - AVD sin tareas: semáforo (verde/amarillo/rojo) por actividad guardado y persistente.
- Botones corregidos: “Volver a datos personales” y “Guardar perfil funcional” (vuelve a Expedientes).

## Entrada
- **Fecha:** 2026-02-01
- **Commit:** feat: perfil funcional completo con evaluación y PDF
- **Zona tocada:** perfil funcional, Room, navegación, UI, generación PDF

### Qué hice
- Completé la implementación del Perfil Funcional asociado a cada persona.
- Añadí selección de diagnóstico CIE-10 (uno por persona).
- Añadí selección múltiple de funcionalidades CIF con límite y persistencia.
- Implementé evaluación de AVD mediante semáforo (rojo, amarillo, verde).
- Guardé todas las selecciones y valoraciones en Room.
- Implementé cálculo automático del resultado según número de rojos, amarillos y verdes.
- Generé informe PDF resumen de la valoración.
- Estabilicé la navegación: login → expedientes → datos personales → perfil funcional → volver/finalizar.
- Corregí errores de KSP, imports, factories de ViewModel e inicialización de base de datos.
- Realicé pequeños ajustes de UI para mejorar legibilidad.

### Estado actual
- La app permite crear expedientes, introducir datos personales, realizar valoración funcional completa y obtener un resultado con informe exportable.

### Pendiente
- Revisión general.
- Pequeñas mejoras estéticas.
- Preparación de documentación, presentación y vídeo.
