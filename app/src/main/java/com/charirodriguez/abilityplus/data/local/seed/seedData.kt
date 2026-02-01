package com.charirodriguez.abilityplus.data.local.seed

import com.charirodriguez.abilityplus.data.local.entity.seed.*

object SeedData {

    val diagnosticosCie10 = listOf(
        DiagnosticoCie10Entity("G30", "Enfermedad de Alzheimer"),
        DiagnosticoCie10Entity("Z89", "Amputación de miembros inferiores"),
        DiagnosticoCie10Entity("H54", "Ceguera / déficit visual grave")
    )

    val funcionalidadesCif = listOf(
        FuncionalidadCifEntity("b144", "Memoria"),
        FuncionalidadCifEntity("b140", "Atención"),
        FuncionalidadCifEntity("b164", "Funciones ejecutivas / planificación"),
        FuncionalidadCifEntity("b114", "Orientación"),
        FuncionalidadCifEntity("b126", "Temperamento y personalidad"),
        FuncionalidadCifEntity("b152", "Funciones emocionales"),
        FuncionalidadCifEntity("b710", "Movilidad de las articulaciones"),
        FuncionalidadCifEntity("b730", "Fuerza muscular"),
        FuncionalidadCifEntity("b760", "Control del movimiento voluntario"),
        FuncionalidadCifEntity("b455", "Tolerancia al esfuerzo"),
        FuncionalidadCifEntity("b770", "Funciones de la marcha"),
        FuncionalidadCifEntity("b210", "Funciones de la visión"),
        FuncionalidadCifEntity("b230", "Funciones de la audición"),
        FuncionalidadCifEntity("b130", "Energía e impulso")
    )

    val actividadesBvd = listOf(
        ActividadBvdEntity(1, "Comer y beber"),
        ActividadBvdEntity(2, "Higiene personal relacionada con micción y defecación"),
        ActividadBvdEntity(3, "Lavarse"),
        ActividadBvdEntity(4, "Realizar otros cuidados corporales"),
        ActividadBvdEntity(5, "Vestirse"),
        ActividadBvdEntity(6, "Mantenimiento de la salud"),
        ActividadBvdEntity(7, "Cambiar y mantener la posición del cuerpo"),
        ActividadBvdEntity(8, "Desplazarse dentro del hogar"),
        ActividadBvdEntity(9, "Desplazarse fuera del hogar"),
        ActividadBvdEntity(10, "Realizar tareas domésticas"),
        ActividadBvdEntity(11, "Tomar decisiones")
    )

    val tareasBvd = emptyList<TareaBvdEntity>()
}
