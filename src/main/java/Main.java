
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Map<Integer, Empleado> empleados = new LinkedHashMap<>();
            SistemaFichado sistema = new SistemaFichado();
            boolean continuar = true;

            while (continuar) {
                mostrarMenu();
                int opcion = leerEntero(scanner, "Seleccione una opcion: ");

                switch (opcion) {
                    case 1 -> agregarEmpleado(scanner, empleados);
                    case 2 -> mostrarEmpleados(empleados);
                    case 3 -> registrarFichada(scanner, empleados, sistema);
                    case 4 -> mostrarHistorial(scanner, empleados);
                    case 0 -> {
                        continuar = false;
                        System.out.println("Hasta luego.");
                    }
                    default -> System.out.println("Opcion no valida. Intente nuevamente.");
                }
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n=== SISTEMA DE FICHADO FAUBA ===");
        System.out.println("1. Agregar empleado");
        System.out.println("2. Listar empleados");
        System.out.println("3. Registrar fichada");
        System.out.println("4. Ver historial de un empleado");
        System.out.println("0. Salir");
    }

    private static void agregarEmpleado(Scanner scanner, Map<Integer, Empleado> empleados) {
        int legajo = leerEntero(scanner, "Ingrese el legajo: ");
        if (empleados.containsKey(legajo)) {
            System.out.println("Ya existe un empleado con ese legajo.");
            return;
        }

        System.out.print("Ingrese el nombre: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }

        try {
            Empleado empleado = new Empleado(legajo, nombre);
            empleados.put(legajo, empleado);
            System.out.println("Empleado agregado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo agregar el empleado: " + e.getMessage());
        }
    }

    private static void mostrarEmpleados(Map<Integer, Empleado> empleados) {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados cargados.");
            return;
        }

        System.out.println("\nEmpleados:");
        for (Empleado empleado : empleados.values()) {
            System.out.println(empleado.getLegajo() + " - " + empleado.getNombre());
        }
    }

    private static void registrarFichada(Scanner scanner, Map<Integer, Empleado> empleados,
                                         SistemaFichado sistema) {
        Empleado empleado = buscarEmpleado(scanner, empleados);
        if (empleado == null) {
            return;
        }

        boolean estaEnFauba = leerPresencia(scanner);
        sistema.fichar(empleado, LocalDateTime.now(), estaEnFauba);
    }

    private static void mostrarHistorial(Scanner scanner, Map<Integer, Empleado> empleados) {
        Empleado empleado = buscarEmpleado(scanner, empleados);
        if (empleado != null) {
            empleado.mostrarFichadas();
        }
    }

    private static Empleado buscarEmpleado(Scanner scanner, Map<Integer, Empleado> empleados) {
        if (empleados.isEmpty()) {
            System.out.println("Primero debe agregar al menos un empleado.");
            return null;
        }

        int legajo = leerEntero(scanner, "Ingrese el legajo del empleado: ");
        Empleado empleado = empleados.get(legajo);
        if (empleado == null) {
            System.out.println("No se encontro un empleado con ese legajo.");
        }
        return empleado;
    }

    private static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    private static boolean leerPresencia(Scanner scanner) {
        while (true) {
            System.out.print("El empleado esta presencialmente en FAUBA? (s/n): ");
            String respuesta = scanner.nextLine().trim();
            if (respuesta.equalsIgnoreCase("s")) {
                return true;
            }
            if (respuesta.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Responda s o n.");
        }
    }
}
