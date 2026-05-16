import { useEffect, useMemo, useState } from 'react';
import {
  createPatient,
  fetchAllCitas,
  fetchPatientWithCitas,
  fetchPatients
} from './hooks/usePatientApi';
import PatientList from './components/PatientList';
import PatientDetail from './components/PatientDetail';
import PatientForm from './components/PatientForm';
import Login from './components/Login';
import CitasPublic from './components/CitasPublic';
import AppointmentForm from './components/AppointmentForm';
import MicroservicesPanel from './components/MicroservicesPanel';
import logo from './assets/hospital-red-norte-logo.svg';

const AUTH_USER_KEY = 'rednorte-user';
const protectedPages = ['citas', 'pacientes', 'lista-espera', 'reportes', 'configuracion'];

function App() {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem(AUTH_USER_KEY);
    return stored ? JSON.parse(stored) : null;
  });
  const [activePage, setActivePage] = useState('pacientes');
  const [redirectPage, setRedirectPage] = useState(null);
  const [authError, setAuthError] = useState('');
  const [patients, setPatients] = useState([]);
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [citas, setCitas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [searchTerm, setSearchTerm] = useState('');

  const goToPage = (page) => {
    if (protectedPages.includes(page) && !user) {
      setRedirectPage(page);
      setAuthError('Solo médicos pueden acceder a esta sección.');
      setActivePage('pacientes');
      return;
    }

    setError('');
    setSuccess('');
    setAuthError('');
    setActivePage(page);
  };

  const handleLogin = (userData) => {
    const authUser = { role: 'medico', name: 'Medico RedSalud', ...userData };
    setUser(authUser);
    localStorage.setItem(AUTH_USER_KEY, JSON.stringify(authUser));
    setActivePage(redirectPage || 'pacientes');
    setRedirectPage(null);
    setAuthError('');
  };

  const handleAppointmentCreated = (appointment) => {
    setSuccess('Cita agendada exitosamente.');
    setError('');
  };

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem(AUTH_USER_KEY);
    setActivePage('pacientes');
    setError('');
    setSuccess('');
    setAuthError('');
  };

  const loadPatients = async () => {
    setLoading(true);
    try {
      const data = await fetchPatients();
      setPatients(data || []);
      setError('');
    } catch (e) {
      setError('No se pudo cargar la lista de pacientes.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (activePage === 'pacientes') {
      loadPatients();
    }
  }, [activePage]);

  const filteredPatients = useMemo(() => {
    const term = searchTerm.trim().toLowerCase();
    if (!term) {
      return patients;
    }

    return patients.filter((patient) => {
      const fullName = `${patient.nombre} ${patient.apellido}`.toLowerCase();
      return fullName.includes(term) || patient.rut?.toLowerCase().includes(term);
    });
  }, [patients, searchTerm]);

  const handleSelectPatient = async (patientId) => {
    setLoading(true);
    setSelectedPatient(null);
    setCitas([]);
    try {
      const data = await fetchPatientWithCitas(patientId);
      setSelectedPatient(data.paciente);
      setCitas(data.citas || []);
      setError('');
    } catch (e) {
      setError('No se pudieron cargar los detalles del paciente.');
    } finally {
      setLoading(false);
    }
  };

  const handleCreatePatient = async (patient) => {
    setSaving(true);
    setSuccess('');
    try {
      const createdPatient = await createPatient(patient);
      await loadPatients();
      await handleSelectPatient(createdPatient.id);
      setSuccess('Paciente agregado correctamente.');
      setError('');
    } catch (e) {
      setError('No se pudo agregar el paciente.');
    } finally {
      setSaving(false);
    }
  };

  if (!user) {
    return <Login onLogin={handleLogin} error={authError} />;
  }

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <img src={logo} alt="Hospital Red Norte" className="brand-logo" />
          <div>
            <strong>Hospital Red Norte</strong>
            <span>Gestion RedSalud</span>
          </div>
        </div>

        <nav className="nav-menu" aria-label="Navegacion principal">
          <a
            className={`nav-item ${activePage === 'citas' ? 'active' : ''}`}
            href="#!"
            onClick={() => goToPage('citas')}
          >
            Citas
          </a>
          <a
            className={`nav-item ${activePage === 'pacientes' ? 'active' : ''}`}
            href="#!"
            onClick={() => goToPage('pacientes')}
          >
            Pacientes
          </a>
          <a
            className={`nav-item ${activePage === 'lista-espera' ? 'active' : ''}`}
            href="#!"
            onClick={() => goToPage('lista-espera')}
          >
            Lista de espera
          </a>
          <a
            className={`nav-item ${activePage === 'reportes' ? 'active' : ''}`}
            href="#!"
            onClick={() => goToPage('reportes')}
          >
            Reportes
          </a>
          <a
            className={`nav-item ${activePage === 'configuracion' ? 'active' : ''}`}
            href="#!"
            onClick={() => goToPage('configuracion')}
          >
            Configuración
          </a>
        </nav>

        <div className="sidebar-status">
          <span className="status-dot"></span>
          {user.name}
        </div>
      </aside>

      <div className="workspace">
        <header className="topbar">
          <div>
            <span className="eyebrow">Panel operativo</span>
            <h1>Gestion Hospital Red Norte</h1>
            <p>
              {activePage === 'citas'
                ? 'Agenda interna para revisar y crear citas medicas.'
                : activePage === 'lista-espera'
                ? 'Gestion priorizada de pacientes en espera y reglas de reasignacion.'
                : 'Panel privado para medicos con acceso a pacientes, citas, reportes y configuracion.'}
            </p>
          </div>

          <div className="topbar-actions">
            <button className="primary-button" type="button" onClick={handleLogout}>
              Cerrar sesion
            </button>
          </div>
        </header>

        {authError && <div className="notice error">{authError}</div>}

        {activePage === 'citas' ? (
          <>
            <CitasPublic />
            <section className="dashboard-grid">
              <section className="panel form-panel">
                <div className="panel-header">
                  <div>
                    <span className="eyebrow">Agenda medica</span>
                    <h2>Formulario de citas</h2>
                  </div>
                </div>
                <AppointmentForm onAppointmentCreated={handleAppointmentCreated} />
              </section>
            </section>
          </>
        ) : activePage === 'pacientes' ? (
          <>
            {(error || success) && (
              <div className={error ? 'notice error' : 'notice success'}>{error || success}</div>
            )}
            <section className="metrics-grid" aria-label="Resumen operacional">
              <article className="metric-card">
                <span>Pacientes activos</span>
                <strong>{patients.length}</strong>
              </article>
              <article className="metric-card">
                <span>Citas del paciente</span>
                <strong>{citas.length}</strong>
              </article>
              <article className="metric-card">
                <span>Estado BFF</span>
                <strong>Online</strong>
              </article>
            </section>
            <main className="dashboard-grid">
              <section className="panel patient-panel" id="pacientes">
                <div className="panel-header">
                  <div>
                    <span className="eyebrow">Registro</span>
                    <h2>Pacientes</h2>
                  </div>
                  {loading && <span className="loading-pill">Actualizando</span>}
                </div>

                <PatientList
                  patients={filteredPatients}
                  onSelect={handleSelectPatient}
                  selectedId={selectedPatient?.id}
                />
              </section>

              <section className="panel detail-panel">
                <div className="panel-header">
                  <div>
                    <span className="eyebrow">Ficha rapida</span>
                    <h2>Detalle clinico</h2>
                  </div>
                </div>
                <PatientDetail patient={selectedPatient} citas={citas} />
              </section>

              <section className="panel form-panel">
                <div className="panel-header">
                  <div>
                    <span className="eyebrow">Nuevo ingreso</span>
                    <h2>Agregar paciente</h2>
                  </div>
                </div>
                <PatientForm onSubmit={handleCreatePatient} saving={saving} />
              </section>
            </main>
          </>
        ) : activePage === 'lista-espera' ? (
          <MicroservicesPanel />
        ) : activePage === 'reportes' ? (
          <section className="dashboard-grid">
            <section className="panel report-panel">
              <div className="panel-header">
                <div>
                  <span className="eyebrow">Reportes</span>
                  <h2>Resumen médico</h2>
                </div>
              </div>
              <div className="metric-list">
                <article className="metric-card">
                  <span>Total de pacientes</span>
                  <strong>{patients.length}</strong>
                </article>
                <article className="metric-card">
                  <span>Citas asignadas</span>
                  <strong>{citas.length}</strong>
                </article>
                <article className="metric-card">
                  <span>Sección privada</span>
                  <strong>Acceso médico</strong>
                </article>
              </div>
            </section>
          </section>
        ) : (
          <section className="dashboard-grid">
            <section className="panel config-panel">
              <div className="panel-header">
                <div>
                  <span className="eyebrow">Configuración</span>
                  <h2>Ajustes del sistema</h2>
                </div>
              </div>
              <div className="panel-content">
                <p>Acceso solo para médicos autenticados.</p>
                <ul>
                  <li>Configuración de pacientes</li>
                  <li>Administración de reportes</li>
                  <li>Preferencias de notificaciones</li>
                </ul>
              </div>
            </section>
          </section>
        )}
      </div>
    </div>
  );
}

export default App;
