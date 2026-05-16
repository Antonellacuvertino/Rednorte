package cl.duoc.rednorte.dto;

public class PacienteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String rut;
    private String historialClinico;

    public PacienteDTO() {}

    public PacienteDTO(Long id, String nombre, String apellido, String rut, String historialClinico) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
        this.historialClinico = historialClinico;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getHistorialClinico() { return historialClinico; }
    public void setHistorialClinico(String historialClinico) { this.historialClinico = historialClinico; }
}