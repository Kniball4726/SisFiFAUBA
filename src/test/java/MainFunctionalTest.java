
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainFunctionalTest {
    @Test
    void pruebaFlujoCompletoDelMenu() throws Exception {
        String entradas = String.join("\n",
                "1",
                "1001",
                "Ana Perez",
                "1",
                "1001",
                "Otro nombre",
                "2",
                "3",
                "1001",
                "x",
                "n",
                "3",
                "1001",
                "s",
                "3",
                "1001",
                "s",
                "4",
                "1001",
                "0",
                "");

        InputStream entradaOriginal = System.in;
        PrintStream salidaOriginal = System.out;
        ByteArrayOutputStream bytesSalida = new ByteArrayOutputStream();
        PrintStream salidaCapturada =
                new PrintStream(bytesSalida, true, StandardCharsets.UTF_8.name());

        try {
            System.setIn(new ByteArrayInputStream(entradas.getBytes(StandardCharsets.UTF_8)));
            System.setOut(salidaCapturada);
            Main.main(new String[0]);
        } finally {
            System.setIn(entradaOriginal);
            System.setOut(salidaOriginal);
            salidaCapturada.close();
        }

        String salida = bytesSalida.toString(StandardCharsets.UTF_8.name());
        verificarContiene(salida, "Empleado agregado correctamente.",
                "Debe permitir dar de alta a un empleado.");
        verificarContiene(salida, "Ya existe un empleado con ese legajo.",
                "Debe rechazar legajos duplicados.");
        verificarContiene(salida, "1001 - Ana Perez",
                "Debe listar el empleado cargado.");
        verificarContiene(salida, "Responda s o n.",
                "Debe pedir nuevamente una respuesta de presencia invalida.");
        verificarContiene(salida, "RECHAZADA: el empleado no esta presencialmente en FAUBA.",
                "No debe registrar una fichada si el empleado no esta en FAUBA.");
        assertTrue(salida.matches("(?s).*Registrada: \\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2} - INGRESO - NORMAL.*"),
                "Debe mostrar la fecha como DD/MM/AAAA y la hora como HH:MM:SS.");
        assertTrue(!salida.contains("Fecha y hora"),
                "No debe solicitar manualmente la fecha y la hora.");
        assertTrue(salida.matches("(?s).*Historial completo de Ana Perez \\(1001\\) \\(2 fichadas\\):\\R"
                        + "1\\. \\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2} - INGRESO - NORMAL\\R"
                        + "2\\. \\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2} - EGRESO - NORMAL.*"),
                "El historial debe incluir, en orden, todas las fichadas del empleado.");
        verificarContiene(salida, "Hasta luego.",
                "Debe cerrar el menu al elegir salir.");
        System.out.println("Prueba funcional aprobada: flujo de alta, validaciones, fichada, historial y salida.");
    }

    private static void verificarContiene(String salida, String esperado, String mensaje) {
        assertTrue(salida.contains(esperado),
                mensaje + " No se encontro en la salida: " + esperado);
    }
}
