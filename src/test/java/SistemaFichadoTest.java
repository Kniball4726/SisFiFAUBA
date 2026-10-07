
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SistemaFichadoTest {
    private static final SistemaFichado SISTEMA = new SistemaFichado();

    @Test
    void pruebaIngresoEgresoNormalYReingreso() {
        probarIngresoEgresoNormalYReingreso();
    }

    @Test
    void pruebaLimitesDeDuracion() {
        probarLimitesDeDuracion();
    }

    @Test
    void pruebaIngresoSinEgresoDespuesDeVeinteHoras() {
        probarIngresoSinEgresoDespuesDeVeinteHoras();
    }

    @Test
    void pruebaRechazosSinModificarElHistorial() {
        probarRechazosSinModificarElHistorial();
    }

    @Test
    void pruebaValidacionesYVistaDeSoloLectura() {
        probarValidacionesYVistaDeSoloLectura();
    }

    @Test
    void pruebaFormatoDeFechaYHora() {
        Fichada fichada = new Fichada(
                LocalDateTime.of(2026, 9, 10, 8, 30, 5),
                TipoFichada.INGRESO,
                EstadoFichada.NORMAL);

        assertTrue(fichada.toString().startsWith("10/09/2026 08:30:05"),
                "La fichada debe mostrar DD/MM/AAAA y HH:MM:SS.");
    }

    private static void probarIngresoEgresoNormalYReingreso() {
        Empleado empleado = nuevoEmpleado(1);
        LocalDateTime ingreso = fecha(8, 30);

        Fichada primera = SISTEMA.fichar(empleado, ingreso, true);
        verificarFichada(primera, ingreso, TipoFichada.INGRESO, EstadoFichada.NORMAL);

        LocalDateTime egreso = ingreso.plusHours(12);
        Fichada segunda = SISTEMA.fichar(empleado, egreso, true);
        verificarFichada(segunda, egreso, TipoFichada.EGRESO, EstadoFichada.NORMAL);

        LocalDateTime reingreso = egreso.plusMinutes(1);
        Fichada tercera = SISTEMA.fichar(empleado, reingreso, true);
        verificarFichada(tercera, reingreso, TipoFichada.INGRESO, EstadoFichada.NORMAL);
        verificar(empleado.getFichadas().size() == 3, "Deben conservarse las tres fichadas.");
    }

    private static void probarLimitesDeDuracion() {
        Empleado masDeDoceHoras = nuevoEmpleado(2);
        LocalDateTime inicio = fecha(8, 30);
        SISTEMA.fichar(masDeDoceHoras, inicio, true);

        Fichada egresoObservado = SISTEMA.fichar(
                masDeDoceHoras, inicio.plusHours(12).plusMinutes(1), true);
        verificarFichada(egresoObservado, inicio.plusHours(12).plusMinutes(1),
                TipoFichada.EGRESO, EstadoFichada.OBSERVADO);

        Empleado veinteHoras = nuevoEmpleado(3);
        SISTEMA.fichar(veinteHoras, inicio, true);
        Fichada egresoEnLimite = SISTEMA.fichar(veinteHoras, inicio.plusHours(20), true);
        verificarFichada(egresoEnLimite, inicio.plusHours(20),
                TipoFichada.EGRESO, EstadoFichada.OBSERVADO);
    }

    private static void probarIngresoSinEgresoDespuesDeVeinteHoras() {
        Empleado empleado = nuevoEmpleado(4);
        LocalDateTime ingresoInicial = fecha(8, 30);
        SISTEMA.fichar(empleado, ingresoInicial, true);

        LocalDateTime nuevoIngreso = ingresoInicial.plusHours(20).plusMinutes(1);
        Fichada fichada = SISTEMA.fichar(empleado, nuevoIngreso, true);

        verificarFichada(fichada, nuevoIngreso, TipoFichada.INGRESO, EstadoFichada.NORMAL);
        verificar(empleado.getFichadas().size() == 2,
                "Un ingreso con mas de 20 horas debe quedar sin egreso y conservarse.");
        verificar(empleado.getFichadas().get(0).getTipo() == TipoFichada.INGRESO,
                "La fichada original debe seguir siendo un ingreso.");
    }

    private static void probarRechazosSinModificarElHistorial() {
        Empleado empleado = nuevoEmpleado(5);
        LocalDateTime ingreso = fecha(8, 30);
        SISTEMA.fichar(empleado, ingreso, true);

        verificar(SISTEMA.fichar(empleado, ingreso.plusHours(1), false) == null,
                "Una fichada fuera de FAUBA debe ser rechazada.");
        verificar(SISTEMA.fichar(empleado, ingreso, true) == null,
                "Una fecha igual a la ultima fichada debe ser rechazada.");
        verificar(SISTEMA.fichar(empleado, ingreso.minusMinutes(1), true) == null,
                "Una fecha anterior a la ultima fichada debe ser rechazada.");
        verificar(empleado.getFichadas().size() == 1,
                "Los rechazos no deben modificar el historial.");

        Fichada posterior = SISTEMA.fichar(empleado, ingreso.plusHours(1), true);
        verificarFichada(posterior, ingreso.plusHours(1),
                TipoFichada.EGRESO, EstadoFichada.NORMAL);
    }

    private static void probarValidacionesYVistaDeSoloLectura() {
        verificarExcepcion(IllegalArgumentException.class,
                () -> new Empleado(0, "Nombre"), "El legajo debe ser positivo.");
        verificarExcepcion(IllegalArgumentException.class,
                () -> new Empleado(1, "  "), "El nombre no debe estar vacio.");
        verificar(nuevoEmpleado(7).getNombre().equals("Empleado 7"),
                "El nombre del empleado debe conservar el valor validado.");
        verificarExcepcion(NullPointerException.class,
                () -> new Fichada(null, TipoFichada.INGRESO, EstadoFichada.NORMAL),
                "La fecha de una fichada es obligatoria.");

        Empleado empleado = nuevoEmpleado(6);
        SISTEMA.fichar(empleado, fecha(8, 30), true);
        List<Fichada> fichadas = empleado.getFichadas();
        verificarExcepcion(UnsupportedOperationException.class, fichadas::clear,
                "El historial publicado debe ser de solo lectura.");
    }

    private static Empleado nuevoEmpleado(int legajo) {
        return new Empleado(legajo, "Empleado " + legajo);
    }

    private static LocalDateTime fecha(int hora, int minuto) {
        return LocalDateTime.of(2026, 9, 10, hora, minuto);
    }

    private static void verificarFichada(Fichada fichada, LocalDateTime fechaHora,
                                         TipoFichada tipo, EstadoFichada estado) {
        verificar(fichada != null, "La fichada esperada no debe ser nula.");
        verificar(fichada.getFechaHora().equals(fechaHora), "La fecha/hora no coincide.");
        verificar(fichada.getTipo() == tipo, "El tipo de fichada no coincide.");
        verificar(fichada.getEstado() == estado, "El estado de fichada no coincide.");
    }

    private static void verificar(boolean condicion, String mensaje) {
        assertTrue(condicion, mensaje);
    }

    private static void verificarExcepcion(Class<? extends Throwable> tipoEsperado,
                                           Runnable accion, String mensaje) {
        assertThrows(tipoEsperado, accion::run, mensaje);
    }
}
