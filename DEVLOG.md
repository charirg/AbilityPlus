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
