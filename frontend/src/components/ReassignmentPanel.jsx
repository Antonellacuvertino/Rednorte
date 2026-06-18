import { useEffect, useState } from 'react';
import { CalendarClock, History } from 'lucide-react';
import {
  fetchAllCitas,
  fetchReassignments,
  reassignAppointment
} from '../hooks/usePatientApi';

function ReassignmentPanel({ doctorName, onReassigned }) {
  const [citas, setCitas] = useState([]);
  const [history, setHistory] = useState([]);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState({
    citaId: '',
    fechaNueva: '',
    horaNueva: '',
    motivo: ''
  });

  const loadData = async () => {
    const [appointmentData, historyData] = await Promise.all([
      fetchAllCitas(),
      fetchReassignments()
    ]);
    setCitas(appointmentData || []);
    setHistory(historyData || []);
  };

  useEffect(() => {
    loadData().catch(() => setError('No se pudo cargar la informacion de reprogramacion.'));
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSaving(true);
    setError('');
    setMessage('');
    try {
      await reassignAppointment({
        ...form,
        citaId: Number(form.citaId),
        medicoResponsable: doctorName
      });
      setForm({ citaId: '', fechaNueva: '', horaNueva: '', motivo: '' });
      setMessage('La cita fue reprogramada y quedo registrada en el historial.');
      await loadData();
      onReassigned?.();
    } catch {
      setError('No fue posible reprogramar la cita. Revisa los datos ingresados.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="schedule-grid">
      <section className="panel">
        <div className="panel-header">
          <div>
            <span className="section-kicker">Gestion de agenda</span>
            <h2>Reprogramar cita</h2>
            <p className="panel-description">Registra el atraso o contingencia y asigna un nuevo horario.</p>
          </div>
          <CalendarClock size={22} aria-hidden="true" />
        </div>

        {(message || error) && <div className={error ? 'notice error' : 'notice success'}>{error || message}</div>}

        <form className="patient-form" onSubmit={handleSubmit}>
          <label className="field">
            <span>Cita a modificar</span>
            <select
              value={form.citaId}
              onChange={(event) => setForm({ ...form, citaId: event.target.value })}
              required
            >
              <option value="">Seleccionar cita</option>
              {citas.map((cita) => (
                <option key={cita.id} value={cita.id}>
                  #{cita.id} - Paciente {cita.pacienteId} - {cita.fecha} {cita.hora}
                </option>
              ))}
            </select>
          </label>
          <div className="form-grid">
            <label className="field">
              <span>Nueva fecha</span>
              <input
                type="date"
                min={new Date().toISOString().split('T')[0]}
                value={form.fechaNueva}
                onChange={(event) => setForm({ ...form, fechaNueva: event.target.value })}
                required
              />
            </label>
            <label className="field">
              <span>Nueva hora</span>
              <input
                type="time"
                value={form.horaNueva}
                onChange={(event) => setForm({ ...form, horaNueva: event.target.value })}
                required
              />
            </label>
          </div>
          <label className="field">
            <span>Motivo de reprogramacion</span>
            <textarea
              rows="3"
              placeholder="Ej: El paciente llego atrasado y requiere un nuevo bloque"
              value={form.motivo}
              onChange={(event) => setForm({ ...form, motivo: event.target.value })}
              required
            />
          </label>
          <button className="primary-button command-button" type="submit" disabled={saving}>
            <CalendarClock size={18} aria-hidden="true" />
            {saving ? 'Guardando cambio...' : 'Confirmar nuevo horario'}
          </button>
        </form>
      </section>

      <section className="panel">
        <div className="panel-header">
          <div>
            <span className="section-kicker">Trazabilidad</span>
            <h2>Historial de cambios</h2>
            <p className="panel-description">Ultimas modificaciones realizadas por el equipo medico.</p>
          </div>
          <History size={22} aria-hidden="true" />
        </div>
        <div className="timeline">
          {history.length ? history.map((item) => (
            <article className="timeline-item" key={item.id}>
              <div className="timeline-dot" />
              <div>
                <div className="timeline-title">
                  <strong>Cita #{item.citaId}</strong>
                  <span className="status-badge">Confirmada</span>
                </div>
                <p>{item.fechaAnterior} {item.horaAnterior} - {item.fechaNueva} {item.horaNueva}</p>
                <small>{item.motivo} - {item.medicoResponsable}</small>
              </div>
            </article>
          )) : (
            <div className="empty-state">
              <strong>Sin reprogramaciones</strong>
              <span>Los cambios manuales apareceran aqui.</span>
            </div>
          )}
        </div>
      </section>
    </div>
  );
}

export default ReassignmentPanel;
