package cl.duoc.rednorte.mapper;

import cl.duoc.rednorte.dto.PacienteDTO;
import cl.duoc.rednorte.entity.Paciente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PacienteMapper {
    PacienteMapper INSTANCE = Mappers.getMapper(PacienteMapper.class);

    PacienteDTO pacienteToPacienteDTO(Paciente paciente);

    Paciente pacienteDTOToPaciente(PacienteDTO pacienteDTO);
}