import { useEffect, useState } from 'react';
import { CalendarRange } from 'lucide-react';
import { fetchAllCitas } from '../hooks/usePatientApi';

function CitasPublic({ refreshKey = 0 }) {
  const [citas, setCitas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setLoading(true);
    fetchAllCitas()
      .then((data) => {
        setCitas(data || []);
        setError('');
      })
      .catch(() => setError('No se pudo cargar la agenda.'))
      .finally(() => setLoading(false));
  }, [refreshKey]);

  return (
    <section className="panel">
      <div className="panel-header">
        <div>
          <span className="section-kicker">Agenda vigente</span>
          <h2>Proximas citas</h2>
          <p className="panel-description">Bloques confirmados para el equipo clinico.</p>
        </div>
        <CalendarRange size={22} />
      </div>

      {loading ? (
        <div className="empty-state"><strong>Cargando citas...</strong></div>
      ) : error ? (
        <div className="notice error">{error}</div>
      ) : citas.length ? (
        <div className="appointment-list">
          {citas.map((cita) => (
            <article className="appointment-card" key={cita.id}>
              <div>
                <strong>{cita.fecha} - {cita.hora || 'Hora por confirmar'}</strong>
                <span>{cita.especialidad || 'Medicina general'}</span>
              </div>
              <span className="appointment-id">Paciente #{cita.pacienteId}</span>
            </article>
          ))}
        </div>
      ) : (
        <div className="empty-state">
          <strong>No hay citas programadas</strong>
          <span>Utiliza el formulario para crear el primer bloque.</span>
        </div>
      )}
    </section>
  );
}

export default CitasPublic;
