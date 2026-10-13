package logica;



import logica.EstadoFichada;
import logica.Empleado;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SistemaFichado {
    public Fichada fichar(Empleado empleado, LocalDateTime fechaHora, boolean estaEnFauba) {
        Objects.requireNonNull(empleado, "El empleado es obligatorio.");
        Objects.requireNonNull(fechaHora, "La fecha y hora son obligatorias.");

        if (!estaEnFauba) {
            System.out.println("RECHAZADA: el empleado no esta presencialmente en FAUBA.");
            return null;
        }

        Fichada ultima = empleado.obtenerUltimaFichada();
        if (ultima == null) {
            return registrar(empleado, fechaHora, TipoFichada.INGRESO, EstadoFichada.NORMAL);
        }

        if (!fechaHora.isAfter(ultima.getFechaHora())) {
            System.out.println("RECHAZADA: la fecha y hora deben ser posteriores a la ultima fichada.");
            return null;
        }

        if (ultima.getTipo() == TipoFichada.EGRESO) {
            return registrar(empleado, fechaHora, TipoFichada.INGRESO, EstadoFichada.NORMAL);
        }

        Duration transcurrido = Duration.between(ultima.getFechaHora(), fechaHora);
        if (transcurrido.compareTo(Duration.ofHours(20)) > 0) {
            System.out.println("AVISO: el ingreso anterior queda sin egreso (mas de 20 horas).");
            return registrar(empleado, fechaHora, TipoFichada.INGRESO, EstadoFichada.NORMAL);
        }

        if (transcurrido.compareTo(Duration.ofHours(12)) > 0) {
            return registrar(empleado, fechaHora, TipoFichada.EGRESO, EstadoFichada.OBSERVADO);
        }

        return registrar(empleado, fechaHora, TipoFichada.EGRESO, EstadoFichada.NORMAL);
    }

    private Fichada registrar(Empleado empleado, LocalDateTime fechaHora,
                              TipoFichada tipo, EstadoFichada estado) {
        Fichada fichada = new Fichada(fechaHora, tipo, estado);
        empleado.agregarFichada(fichada);
        System.out.println("Registrada: " + fichada);
        return fichada;
    }
}
