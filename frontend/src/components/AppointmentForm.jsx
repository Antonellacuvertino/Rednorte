import { useState, useEffect } from 'react';
import { createCita, fetchPatients } from '../hooks/usePatientApi';
import './AppointmentForm.css';

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

  const getPacienteNombre = (id) => {
    const paciente = pacientes.find(p => p.id === parseInt(id));
    return paciente ? `${paciente.nombre} ${paciente.apellido}` : '';
  };

  return (
    <div className="appointment-form-container">
      <div className="appointment-form-card">
        <h3>Agendar Nueva Cita</h3>

        <form onSubmit={handleSubmit} className="appointment-form">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="pacienteId">Paciente:</label>
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
            </div>

            <div className="form-group">
              <label htmlFor="especialidad">Especialidad:</label>
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
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="fecha">Fecha:</label>
              <input
                type="date"
                id="fecha"
                name="fecha"
                value={formData.fecha}
                onChange={handleChange}
                min={new Date().toISOString().split('T')[0]}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="hora">Hora:</label>
              <input
                type="time"
                id="hora"
                name="hora"
                value={formData.hora}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="motivo">Motivo de la consulta:</label>
            <textarea
              id="motivo"
              name="motivo"
              value={formData.motivo}
              onChange={handleChange}
              placeholder="Describa brevemente el motivo de la consulta"
              rows="3"
              required
            />
          </div>

          <button type="submit" disabled={loading} className="submit-button">
            {loading ? 'Agendando...' : 'Agendar Cita'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default AppointmentForm;