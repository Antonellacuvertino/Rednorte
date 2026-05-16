package cl.duoc.rednorte.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    private Long id;
    private String nombre;
    private String apellido;
    private String rut;
    private String historialClinico;
}
