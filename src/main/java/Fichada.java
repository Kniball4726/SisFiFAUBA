

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class Fichada {
    private final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    @Id
    private final LocalDateTime fechaHora;
    @Column
    private final TipoFichada tipo;
    @Column
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
