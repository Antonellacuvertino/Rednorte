import { useEffect, useState } from 'react';
import { fetchAllCitas } from '../hooks/usePatientApi';

function CitasPublic() {
  const [citas, setCitas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const loadCitas = async () => {
      setLoading(true);
      try {
        const data = await fetchAllCitas();
        setCitas(data || []);
        setError('');
      } catch (e) {
        setError('No se pudieron cargar las citas internas.');
      } finally {
        setLoading(false);
      }
    };

    loadCitas();
  }, []);

  return (
    <div className="dashboard-grid">
      <section className="panel public-panel">
        <div className="panel-header">
          <div>
            <span className="eyebrow">Agenda medica</span>
            <h2>Agenda disponible</h2>
          </div>
        </div>

        {loading ? (
          <div className="empty-state">
            <strong>Cargando citas...</strong>
          </div>
        ) : error ? (
          <div className="notice error">{error}</div>
        ) : citas.length ? (
          <div className="appointment-list">
            {citas.map((cita) => (
              <article className="appointment-card" key={cita.id}>
                <div>
                  <strong>{cita.fecha}</strong>
                  <span>{cita.hora || 'Hora por confirmar'}</span>
                </div>
                <p>Paciente ID #{cita.pacienteId}</p>
              </article>
            ))}
          </div>
        ) : (
          <div className="empty-state">
            <strong>No hay citas disponibles</strong>
            <span>Aun no hay citas registradas para el equipo medico.</span>
          </div>
        )}
      </section>
    </div>
  );
}

export default CitasPublic;
