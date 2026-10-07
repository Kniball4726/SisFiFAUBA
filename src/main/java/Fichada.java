

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Fichada {
    private static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final LocalDateTime fechaHora;
    private final TipoFichada tipo;
    private final EstadoFichada estado;

    public Fichada(LocalDateTime fechaHora, TipoFichada tipo, EstadoFichada estado) {
        this.fechaHora = Objects.requireNonNull(fechaHora, "La fecha y hora son obligatorias.");
        this.tipo = Objects.requireNonNull(tipo, "El tipo de fichada es obligatorio.");
        this.estado = Objects.requireNonNull(estado, "El estado de la fichada es obligatorio.");
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public TipoFichada getTipo() {
        return tipo;
    }

    public EstadoFichada getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return fechaHora.format(FORMATO_FECHA_HORA) + " - " + tipo + " - " + estado;
    }
}
