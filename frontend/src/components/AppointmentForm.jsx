import { useState, useEffect } from 'react';
import { createCita, fetchPatients } from '../hooks/usePatientApi';

function AppointmentForm({ onAppointmentCreated }) {
  const [formData, setFormData] = useState({
    pacienteId: '',
    especialidad: '',
    fecha: '',
    hora: '',
    motivo: ''
  });
  const [loading, setLoading] = useState(false);
  const [pacientes, setPacientes] = useState([]);
  const [especialidades] = useState([
    'CARDIOLOGIA',
    'PEDIATRIA',
    'TRAUMATOLOGIA',
    'GINECOLOGIA',
    'OFTALMOLOGIA',
    'DERMATOLOGIA'
  ]);

  useEffect(() => {
    loadPacientes();
  }, []);

  const loadPacientes = async () => {
    try {
      const data = await fetchPatients();
      setPacientes(data || []);
    } catch (error) {
      console.error('Error cargando pacientes:', error);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const appointmentData = {
        pacienteId: parseInt(formData.pacienteId),
        especialidad: formData.especialidad,
        fecha: formData.fecha,
        hora: formData.hora,
        motivo: formData.motivo,
        estado: 'PROGRAMADA'
      };

      const newAppointment = await createCita(appointmentData);
      onAppointmentCreated(newAppointment);

      // Limpiar formulario
      setFormData({
        pacienteId: '',
        especialidad: '',
        fecha: '',
        hora: '',
        motivo: ''
      });
      alert('Cita agendada exitosamente');
    } catch (error) {
      console.error('Error:', error);
      alert('Error al conectar con el servidor');
    }

    setLoading(false);
  };

  return (
    <form onSubmit={handleSubmit} className="patient-form">
          <div className="form-grid">
            <label className="field" htmlFor="pacienteId">
              <span>Paciente</span>
              <select
                id="pacienteId"
                name="pacienteId"
                value={formData.pacienteId}
                onChange={handleChange}
                required
              >
                <option value="">Seleccionar paciente</option>
                {pacientes.map(paciente => (
                  <option key={paciente.id} value={paciente.id}>
                    {paciente.nombre} {paciente.apellido} - {paciente.rut}
                  </option>
                ))}
              </select>
            </label>

            <label className="field" htmlFor="especialidad">
              <span>Especialidad</span>
              <select
                id="especialidad"
                name="especialidad"
                value={formData.especialidad}
                onChange={handleChange}
                required
              >
                <option value="">Seleccionar especialidad</option>
                {especialidades.map(esp => (
                  <option key={esp} value={esp}>{esp}</option>
                ))}
              </select>
            </label>
          </div>

          <div className="form-grid">
            <label className="field" htmlFor="fecha">
              <span>Fecha</span>
              <input
                type="date"
                id="fecha"
                name="fecha"
                value={formData.fecha}
                onChange={handleChange}
                min={new Date().toISOString().split('T')[0]}
                required
              />
            </label>

            <label className="field" htmlFor="hora">
              <span>Hora</span>
              <input
                type="time"
                id="hora"
                name="hora"
                value={formData.hora}
                onChange={handleChange}
                required
              />
            </label>
          </div>

          <label className="field" htmlFor="motivo">
            <span>Motivo de la consulta</span>
            <textarea
              id="motivo"
              name="motivo"
              value={formData.motivo}
              onChange={handleChange}
              placeholder="Describa brevemente el motivo de la consulta"
              rows="3"
              required
            />
          </label>

          <button type="submit" disabled={loading} className="primary-button">
            {loading ? 'Agendando...' : 'Agendar Cita'}
          </button>
        </form>
  );
}

export default AppointmentForm;
