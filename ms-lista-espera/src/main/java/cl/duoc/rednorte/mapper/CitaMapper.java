package cl.duoc.rednorte.mapper;

import cl.duoc.rednorte.dto.CitaDTO;
import cl.duoc.rednorte.entity.Cita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CitaMapper {

    @Mapping(source = "paciente.id", target = "pacienteId")
    CitaDTO toDto(Cita cita);

    Cita toEntity(CitaDTO citaDTO);
}