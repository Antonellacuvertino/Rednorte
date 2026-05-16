package cl.duoc.rednorte.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cita {
    private Long id;
    private String fecha;
    private String hora;
    private Paciente paciente;
}
