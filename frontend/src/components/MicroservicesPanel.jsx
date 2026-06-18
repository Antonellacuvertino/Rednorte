import { useEffect, useState } from 'react';
import { Bell, ClipboardList, Radio, UserRoundCheck } from 'lucide-react';
import {
  createListaEspera,
  fetchAuditEvents,
  fetchListaEsperaPendiente,
  fetchNotifications,
  fetchPatients,
  markNotificationAsRead,
  updateListaEsperaEstado
} from '../hooks/usePatientApi';

const especialidades = ['CARDIOLOGIA', 'PEDIATRIA', 'TRAUMATOLOGIA', 'GINECOLOGIA', 'OFTALMOLOGIA', 'DERMATOLOGIA'];
const prioridades = ['ALTA', 'MEDIA', 'BAJA'];

function MicroservicesPanel({ showNotifications = true, autoRefresh = false }) {
  const [patients, setPatients] = useState([]);
  const [waitingList, setWaitingList] = useState([]);
  const [auditEvents, setAuditEvents] = useState([]);
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [form, setForm] = useState({
    pacienteId: '',
    especialidad: 'CARDIOLOGIA',
    prioridad: 'MEDIA',
    observaciones: ''
  });

  const loadData = async () => {
    setLoading(true);
    try {
      const [patientData, waitingData, auditData, notificationData] = await Promise.all([
        fetchPatients(),
        fetchListaEsperaPendiente(),
        fetchAuditEvents(),
        showNotifications ? fetchNotifications() : Promise.resolve([])
      ]);
      setPatients(patientData || []);
      setWaitingList(waitingData || []);
      setAuditEvents(auditData || []);
      setNotifications(notificationData || []);
      setError('');
    } catch {
      setError('No se pudo sincronizar la informacion operativa.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
    if (!autoRefresh) return undefined;
    const interval = window.setInterval(loadData, 30000);
    return () => window.clearInterval(interval);
  }, [autoRefresh, showNotifications]);

  const createWaitingRecord = async (event) => {
    event.preventDefault();
    try {
      await createListaEspera({ ...form, pacienteId: Number(form.pacienteId), estado: 'PENDIENTE' });
      setForm((current) => ({ ...current, pacienteId: '', observaciones: '' }));
      setMessage('Paciente incorporado a la lista de espera.');
      await loadData();
    } catch {
      setError('No se pudo crear el registro de espera.');
    }
  };

  const updateStatus = async (id, action) => {
    await updateListaEsperaEstado(id, action);
    setMessage(action === 'atender' ? 'Paciente marcado como atendido.' : 'Registro cancelado.');
    await loadData();
  };

  const readNotification = async (id) => {
    await markNotificationAsRead(id);
    await loadData();
  };

  return (
    <>
      {(message || error) && <div className={error ? 'notice error' : 'notice success'}>{error || message}</div>}

      <section className="metrics-grid">
        <article className="metric-card">
          <ClipboardList size={19} />
          <span>Pacientes en espera</span>
          <strong>{waitingList.length}</strong>
        </article>
        <article className="metric-card">
          <Bell size={19} />
          <span>Notificaciones pendientes</span>
          <strong>{notifications.filter((item) => !item.readFlag).length}</strong>
        </article>
        <article className="metric-card">
          <Radio size={19} />
          <span>Sincronizacion</span>
          <strong className="metric-status">{loading ? 'Actualizando' : 'Operativa'}</strong>
        </article>
      </section>

      <main className="micro-grid">
        <section className="panel">
          <div className="panel-header">
            <div>
              <span className="section-kicker">Nuevo ingreso</span>
              <h2>Agregar a lista de espera</h2>
            </div>
            <UserRoundCheck size={22} />
          </div>
          <form className="patient-form" onSubmit={createWaitingRecord}>
            <label className="field">
              <span>Paciente</span>
              <select value={form.pacienteId} onChange={(event) => setForm({ ...form, pacienteId: event.target.value })} required>
                <option value="">Seleccionar paciente</option>
                {patients.map((patient) => (
                  <option key={patient.id} value={patient.id}>
                    {patient.nombre} {patient.apellido} - {patient.rut}
                  </option>
                ))}
              </select>
            </label>
            <div className="form-grid">
              <label className="field">
                <span>Especialidad</span>
                <select value={form.especialidad} onChange={(event) => setForm({ ...form, especialidad: event.target.value })}>
                  {especialidades.map((item) => <option key={item}>{item}</option>)}
                </select>
              </label>
              <label className="field">
                <span>Prioridad</span>
                <select value={form.prioridad} onChange={(event) => setForm({ ...form, prioridad: event.target.value })}>
                  {prioridades.map((item) => <option key={item}>{item}</option>)}
                </select>
              </label>
            </div>
            <label className="field">
              <span>Observaciones</span>
              <textarea rows="3" value={form.observaciones} onChange={(event) => setForm({ ...form, observaciones: event.target.value })} />
            </label>
            <button className="primary-button" type="submit">Guardar en lista</button>
          </form>
        </section>

        <section className="panel">
          <div className="panel-header">
            <div>
              <span className="section-kicker">Cola priorizada</span>
              <h2>Atenciones pendientes</h2>
            </div>
          </div>
          <div className="appointment-list">
            {waitingList.length ? waitingList.map((item) => (
              <article className="appointment-card" key={item.id}>
                <div>
                  <strong>{item.especialidad}</strong>
                  <span>Paciente #{item.pacienteId} - Prioridad {item.prioridad}</span>
                  <p>{item.observaciones || 'Sin observaciones'}</p>
                </div>
                <div className="inline-actions">
                  <button type="button" onClick={() => updateStatus(item.id, 'atender')}>Atender</button>
                  <button type="button" onClick={() => updateStatus(item.id, 'cancelar')}>Cancelar</button>
                </div>
              </article>
            )) : <div className="empty-state"><strong>No hay pacientes pendientes</strong></div>}
          </div>
        </section>

        {showNotifications && (
          <section className="panel">
            <div className="panel-header">
              <div>
                <span className="section-kicker">Centro de avisos</span>
                <h2>Notificaciones</h2>
              </div>
            </div>
            <div className="appointment-list">
              {notifications.length ? notifications.slice(0, 6).map((item) => (
                <article className="appointment-card" key={item.id}>
                  <div>
                    <strong>{item.title}</strong>
                    <span>{item.eventType}</span>
                    <p>{item.message}</p>
                  </div>
                  {!item.readFlag && <button type="button" onClick={() => readNotification(item.id)}>Marcar leida</button>}
                </article>
              )) : <div className="empty-state"><strong>Sin notificaciones</strong></div>}
            </div>
          </section>
        )}

        <section className="panel">
          <div className="panel-header">
            <div>
              <span className="section-kicker">Trazabilidad tecnica</span>
              <h2>Auditoria reciente</h2>
            </div>
          </div>
          <div className="audit-table">
            {auditEvents.length ? auditEvents.slice(0, 8).map((item) => (
              <div className="audit-row" key={item.id}>
                <strong>{item.eventType}</strong>
                <span>{new Date(item.occurredAt).toLocaleString()}</span>
              </div>
            )) : <div className="empty-state"><strong>Sin eventos registrados</strong></div>}
          </div>
        </section>
      </main>
    </>
  );
}

export default MicroservicesPanel;
