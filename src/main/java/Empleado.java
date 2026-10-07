

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Empleado {
    private final int legajo;
    private final String nombre;
    private final List<Fichada> fichadas;

    public Empleado(int legajo, String nombre) {
        if (legajo <= 0) {
            throw new IllegalArgumentException("El legajo debe ser un numero positivo.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }

        this.legajo = legajo;
        this.nombre = nombre.trim();
        this.fichadas = new ArrayList<>();
    }

    public int getLegajo() {
        return legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Fichada> getFichadas() {
        return Collections.unmodifiableList(fichadas);
    }

    public void agregarFichada(Fichada fichada) {
        fichadas.add(Objects.requireNonNull(fichada, "La fichada no puede ser nula."));
    }

    public Fichada obtenerUltimaFichada() {
        if (fichadas.isEmpty()) {
            return null;
        }
        return fichadas.get(fichadas.size() - 1);
    }

    public void mostrarFichadas() {
        System.out.println("\nHistorial completo de " + nombre + " (" + legajo
                + ") (" + fichadas.size() + " fichadas):");
        if (fichadas.isEmpty()) {
            System.out.println("Sin fichadas.");
            return;
        }

        for (int i = 0; i < fichadas.size(); i++) {
            System.out.println((i + 1) + ". " + fichadas.get(i));
        }
    }
}
