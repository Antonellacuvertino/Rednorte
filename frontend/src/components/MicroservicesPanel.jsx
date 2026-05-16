import { useEffect, useState } from 'react';
import {
  createListaEspera,
  createReglaReasignacion,
  ejecutarReasignacion,
  fetchListaEsperaPendiente,
  fetchPatients,
  fetchReglasReasignacion,
  inicializarReasignacion,
  updateListaEsperaEstado
} from '../hooks/usePatientApi';

const especialidades = ['CARDIOLOGIA', 'PEDIATRIA', 'TRAUMATOLOGIA', 'GINECOLOGIA', 'OFTALMOLOGIA', 'DERMATOLOGIA'];
const prioridades = ['ALTA', 'MEDIA', 'BAJA'];
const tiposRegla = ['TIEMPO_ESPERA', 'CAPACIDAD_MAXIMA', 'PRIORIDAD'];

function MicroservicesPanel() {
  const [patients, setPatients] = useState([]);
  const [listaEspera, setListaEspera] = useState([]);
  const [reglas, setReglas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [listaForm, setListaForm] = useState({
    pacienteId: '',
    especialidad: 'CARDIOLOGIA',
    prioridad: 'MEDIA',
    observaciones: ''
  });
  const [reglaForm, setReglaForm] = useState({
    especialidad: 'CARDIOLOGIA',
    reglaTipo: 'TIEMPO_ESPERA',
    valor: '30',
    descripcion: ''
  });

  const loadData = async () => {
    setLoading(true);
    try {
      const [patientData, listaData, reglasData] = await Promise.all([
        fetchPatients(),
        fetchListaEsperaPendiente(),
        fetchReglasReasignacion()
      ]);
      setPatients(patientData || []);
      setListaEspera(listaData || []);
      setReglas(reglasData || []);
      setError('');
    } catch (e) {
      setError('No se pudo cargar la lista de espera. Revisa que los servicios esten activos.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleListaSubmit = async (event) => {
    event.preventDefault();
    setMessage('');
    try {
      await createListaEspera({
        ...listaForm,
        pacienteId: Number(listaForm.pacienteId),
        estado: 'PENDIENTE'
      });
      setListaForm((current) => ({ ...current, pacienteId: '', observaciones: '' }));
      setMessage('Paciente agregado a lista de espera.');
      await loadData();
    } catch (e) {
      setError('No se pudo crear el registro de lista de espera.');
    }
  };

  const handleReglaSubmit = async (event) => {
    event.preventDefault();
    setMessage('');
    try {
      await createReglaReasignacion({ ...reglaForm, activa: true });
      setReglaForm((current) => ({ ...current, descripcion: '' }));
      setMessage('Regla de reasignacion creada.');
      await loadData();
    } catch (e) {
      setError('No se pudo crear la regla de reasignacion.');
    }
  };

  const handleListaAction = async (id, action) => {
    setMessage('');
    try {
      await updateListaEsperaEstado(id, action);
      setMessage(action === 'atender' ? 'Registro marcado como atendido.' : 'Registro cancelado.');
      await loadData();
    } catch (e) {
      setError('No se pudo actualizar el registro.');
    }
  };

  const handleReasignacionAction = async (action) => {
    setMessage('');
    try {
      const result = action === 'inicializar' ? await inicializarReasignacion() : await ejecutarReasignacion();
      setMessage(result);
      await loadData();
    } catch (e) {
      setError('No se pudo ejecutar la accion de reasignacion.');
    }
  };

  return (
    <>
      {(message || error) && <div className={error ? 'notice error' : 'notice success'}>{error || message}</div>}

      <section className="metrics-grid">
        <article className="metric-card">
          <span>En espera</span>
          <strong>{listaEspera.length}</strong>
        </article>
        <article className="metric-card">
          <span>Reglas</span>
          <strong>{reglas.length}</strong>
        </article>
        <article className="metric-card">
          <span>Estado</span>
          <strong>{loading ? 'Sync' : 'OK'}</strong>
        </article>
      </section>

      <main className="micro-grid">
        <section className="panel">
          <div className="panel-header">
            <div>
              <span className="eyebrow">Lista de espera</span>
              <h2>Agregar paciente</h2>
            </div>
          </div>
          <form className="patient-form" onSubmit={handleListaSubmit}>
            <label className="field">
              <span>Paciente</span>
              <select value={listaForm.pacienteId} onChange={(e) => setListaForm({ ...listaForm, pacienteId: e.target.value })} required>
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
                <select value={listaForm.especialidad} onChange={(e) => setListaForm({ ...listaForm, especialidad: e.target.value })}>
                  {especialidades.map((item) => <option key={item} value={item}>{item}</option>)}
                </select>
              </label>
              <label className="field">
                <span>Prioridad</span>
                <select value={listaForm.prioridad} onChange={(e) => setListaForm({ ...listaForm, prioridad: e.target.value })}>
                  {prioridades.map((item) => <option key={item} value={item}>{item}</option>)}
                </select>
              </label>
            </div>
            <label className="field field-wide">
              <span>Observaciones</span>
              <textarea rows="3" value={listaForm.observaciones} onChange={(e) => setListaForm({ ...listaForm, observaciones: e.target.value })} />
            </label>
            <button className="primary-button" type="submit">Crear espera</button>
          </form>
        </section>

        <section className="panel">
          <div className="panel-header">
            <div>
              <span className="eyebrow">Pendientes</span>
              <h2>Cola priorizada</h2>
            </div>
          </div>
          <div className="appointment-list">
            {listaEspera.length ? listaEspera.map((item) => (
              <article className="appointment-card" key={item.id}>
                <div>
                  <strong>{item.especialidad}</strong>
                  <span>Paciente #{item.pacienteId} - {item.prioridad}</span>
                  <p>{item.observaciones || 'Sin observaciones'}</p>
                </div>
                <div className="inline-actions">
                  <button type="button" onClick={() => handleListaAction(item.id, 'atender')}>Atender</button>
                  <button type="button" onClick={() => handleListaAction(item.id, 'cancelar')}>Cancelar</button>
                </div>
              </article>
            )) : (
              <div className="empty-state">
                <strong>No hay pacientes pendientes</strong>
                <span>Crea un registro para probar el microservicio.</span>
              </div>
            )}
          </div>
        </section>

        <section className="panel">
          <div className="panel-header">
            <div>
              <span className="eyebrow">Reasignacion</span>
              <h2>Reglas automaticas</h2>
            </div>
          </div>
          <form className="patient-form" onSubmit={handleReglaSubmit}>
            <div className="form-grid">
              <label className="field">
                <span>Especialidad</span>
                <select value={reglaForm.especialidad} onChange={(e) => setReglaForm({ ...reglaForm, especialidad: e.target.value })}>
                  {especialidades.map((item) => <option key={item} value={item}>{item}</option>)}
                </select>
              </label>
              <label className="field">
                <span>Tipo</span>
                <select value={reglaForm.reglaTipo} onChange={(e) => setReglaForm({ ...reglaForm, reglaTipo: e.target.value })}>
                  {tiposRegla.map((item) => <option key={item} value={item}>{item}</option>)}
                </select>
              </label>
            </div>
            <label className="field">
              <span>Valor</span>
              <input value={reglaForm.valor} onChange={(e) => setReglaForm({ ...reglaForm, valor: e.target.value })} required />
            </label>
            <label className="field">
              <span>Descripcion</span>
              <textarea rows="3" value={reglaForm.descripcion} onChange={(e) => setReglaForm({ ...reglaForm, descripcion: e.target.value })} />
            </label>
            <button className="primary-button" type="submit">Crear regla</button>
          </form>
          <div className="split-actions">
            <button type="button" onClick={() => handleReasignacionAction('inicializar')}>Inicializar reglas</button>
            <button type="button" onClick={() => handleReasignacionAction('ejecutar')}>Ejecutar ahora</button>
          </div>
        </section>

        <section className="panel">
          <div className="panel-header">
            <div>
              <span className="eyebrow">Reglas activas</span>
              <h2>Configuracion actual</h2>
            </div>
          </div>
          <div className="appointment-list">
            {reglas.length ? reglas.map((regla) => (
              <article className="appointment-card" key={regla.id}>
                <div>
                  <strong>{regla.especialidad}</strong>
                  <span>{regla.reglaTipo} - Valor {regla.valor}</span>
                  <p>{regla.descripcion || 'Sin descripcion'}</p>
                </div>
                <span className="status-badge">{regla.activa ? 'Activa' : 'Inactiva'}</span>
              </article>
            )) : (
              <div className="empty-state">
                <strong>No hay reglas</strong>
                <span>Inicializa reglas por defecto o crea una nueva.</span>
              </div>
            )}
          </div>
        </section>
      </main>
    </>
  );
}

export default MicroservicesPanel;
